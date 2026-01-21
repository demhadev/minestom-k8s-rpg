# deploy-kind.ps1
# One-button deploy: Gradle fat jar -> Docker image -> kind -> kubectl apply
# Run from project root:  powershell -ExecutionPolicy Bypass -File .\deploy-kind.ps1

$ErrorActionPreference = "Stop"

$projectDir = "C:\Users\Ahmed\IdeaProjects\MinestomStuff"
$kindExe    = "C:\Tools\kind\kind.exe"
$cluster    = "kind"
$configYaml = Join-Path $projectDir "kind-config.yaml"
$k8sYaml    = Join-Path $projectDir "minestom.yaml"
$image      = "minestomstuff:local"

Write-Host "==> Switching to project dir: $projectDir"
Set-Location $projectDir

function Assert-Command($name) {
    if (-not (Get-Command $name -ErrorAction SilentlyContinue)) {
        throw "Required command not found in PATH: $name"
    }
}

Assert-Command "docker"
Assert-Command "kubectl"

if (-not (Test-Path $kindExe)) {
    throw "kind.exe not found at: $kindExe"
}
if (-not (Test-Path $configYaml)) {
    throw "kind-config.yaml not found at: $configYaml"
}
if (-not (Test-Path $k8sYaml)) {
    throw "minestom.yaml not found at: $k8sYaml"
}

Write-Host "`n==> Checking Docker engine..."
docker info | Out-Null
Write-Host "    Docker is running."

Write-Host "`n==> Building fat jar (shadowJar)..."
.\gradlew.bat clean shadowJar

Write-Host "`n==> Building Docker image: $image"
docker build -t $image .

# Ensure kind cluster is reachable; recreate if not.
$needRecreate = $false
try {
    kubectl get nodes | Out-Null
} catch {
    $needRecreate = $true
}

if ($needRecreate) {
    Write-Host "`n==> Kubernetes not reachable. Recreating kind cluster..."
    & $kindExe delete cluster --name $cluster | Out-Null
    & $kindExe create cluster --config $configYaml --name $cluster
}

Write-Host "`n==> Waiting for node to be Ready..."
kubectl wait --for=condition=Ready node/kind-control-plane --timeout=180s

Write-Host "`n==> Loading Docker image into kind..."
& $kindExe load docker-image $image --name $cluster

Write-Host "`n==> Applying Kubernetes manifests..."
kubectl apply -f $k8sYaml

Write-Host "`n==> Rolling out deployment..."
# If deployment doesn't exist yet, rollout status will fail; so we ignore that once.
try {
    kubectl rollout status deployment/minestom --timeout=120s
} catch {
    Write-Host "    rollout status skipped (deployment may be creating)."
}

Write-Host "`n==> Current pods:"
kubectl get pods

Write-Host "`n==> Service:"
kubectl get svc minestom-svc -o wide

Write-Host "`n==> Done."
Write-Host "If you want to connect via kubectl port-forward:"
Write-Host "  kubectl port-forward svc/minestom-svc 25565:25565"

Add-Type -AssemblyName System.IO.Compression.FileSystem

$zipPath = "D:\. Pelajaran\KK\PPB\APK Alkitab\PENJELASAN_KODE_JAVA_LENTERA_KEHIDUPAN.docx"
$zip = [System.IO.Compression.ZipFile]::OpenRead($zipPath)
Write-Output "DAFTAR ENTRY:"
foreach ($item in $zip.Entries) {
    Write-Output " - $($item.FullName)"
}
$zip.Dispose()

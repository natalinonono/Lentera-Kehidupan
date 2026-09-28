$word = New-Object -ComObject Word.Application
$word.Visible = $false
try {
    $doc = $word.Documents.Open("D:\. Pelajaran\KK\PPB\APK Alkitab\PENJELASAN_KODE_JAVA_LENTERA_KEHIDUPAN.docx")
    Write-Output "SUKSES DIBUKA MS WORD: Total paragraf = $($doc.Paragraphs.Count)"
    $doc.Close()
} catch {
    Write-Output "GAGAL: $_"
} finally {
    $word.Quit()
}

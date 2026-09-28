Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

function Create-CleanDocx {
    param (
        [string]$outputPath,
        [string]$documentXmlContent
    )

    if ([System.IO.File]::Exists($outputPath)) {
        [System.IO.File]::Delete($outputPath)
    }

    $zipStream = [System.IO.File]::Open($outputPath, [System.IO.FileMode]::CreateNew)
    $archive = New-Object System.IO.Compression.ZipArchive($zipStream, [System.IO.Compression.ZipArchiveMode]::Create)

    # 1. [Content_Types].xml
    $e1 = $archive.CreateEntry("[Content_Types].xml")
    $sw1 = New-Object System.IO.StreamWriter($e1.Open(), [System.Text.Encoding]::UTF8)
    $sw1.Write(@'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
  <Override PartName="/word/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/>
</Types>
'@)
    $sw1.Close()

    # 2. _rels/.rels
    $e2 = $archive.CreateEntry("_rels/.rels")
    $sw2 = New-Object System.IO.StreamWriter($e2.Open(), [System.Text.Encoding]::UTF8)
    $sw2.Write(@'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>
'@)
    $sw2.Close()

    # 3. word/_rels/document.xml.rels
    $e3 = $archive.CreateEntry("word/_rels/document.xml.rels")
    $sw3 = New-Object System.IO.StreamWriter($e3.Open(), [System.Text.Encoding]::UTF8)
    $sw3.Write(@'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
</Relationships>
'@)
    $sw3.Close()

    # 4. word/styles.xml
    $e4 = $archive.CreateEntry("word/styles.xml")
    $sw4 = New-Object System.IO.StreamWriter($e4.Open(), [System.Text.Encoding]::UTF8)
    $sw4.Write(@'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:styles xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:docDefaults>
    <w:rPrDefault>
      <w:rPr>
        <w:rFonts w:ascii="Calibri" w:hAnsi="Calibri" w:cs="Calibri"/>
        <w:sz w:val="22"/>
        <w:szCs w:val="22"/>
        <w:color w:val="2D3748"/>
      </w:rPr>
    </w:rPrDefault>
    <w:pPrDefault>
      <w:pPr>
        <w:spacing w:line="276" w:lineRule="auto" w:after="120"/>
      </w:pPr>
    </w:pPrDefault>
  </w:docDefaults>
</w:styles>
'@)
    $sw4.Close()

    # 5. word/document.xml
    $e5 = $archive.CreateEntry("word/document.xml")
    $sw5 = New-Object System.IO.StreamWriter($e5.Open(), [System.Text.Encoding]::UTF8)
    $sw5.Write($documentXmlContent)
    $sw5.Close()

    $archive.Dispose()
    $zipStream.Close()
}

$docXml = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:body>
    
    <!-- HEADER DOKUMEN -->
    <w:p>
      <w:pPr>
        <w:jc w:val="center"/>
        <w:spacing w:before="120" w:after="60"/>
      </w:pPr>
      <w:r>
        <w:rPr>
          <w:rFonts w:ascii="Arial" w:hAnsi="Arial"/>
          <w:b/>
          <w:sz w:val="32"/>
          <w:color w:val="1A202C"/>
        </w:rPr>
        <w:t>DOKUMENTASI KODE &amp; BEDAH ARSITEKTUR KELAS</w:t>
      </w:r>
    </w:p>

    <w:p>
      <w:pPr>
        <w:jc w:val="center"/>
        <w:spacing w:before="0" w:after="80"/>
      </w:pPr>
      <w:r>
        <w:rPr>
          <w:rFonts w:ascii="Arial" w:hAnsi="Arial"/>
          <w:b/>
          <w:sz w:val="26"/>
          <w:color w:val="DC2626"/>
        </w:rPr>
        <w:t>APLIKASI &quot;LENTERA KEHIDUPAN&quot; (SELESAIKAN ALKITABMU)</w:t>
      </w:r>
    </w:p>

    <w:p>
      <w:pPr>
        <w:jc w:val="center"/>
        <w:spacing w:before="0" w:after="240"/>
      </w:pPr>
      <w:r>
        <w:rPr>
          <w:sz w:val="20"/>
          <w:i/>
          <w:color w:val="718096"/>
        </w:rPr>
        <w:t>Penjelasan Komprehensif File Inti Java: Controller, Helper, Model, dan Data Engine</w:t>
      </w:r>
    </w:p>

    <!-- RINGKASAN FILE DALAM TABEL -->
    <w:tbl>
      <w:tblPr>
        <w:tblW w:w="9360" w:type="dxa"/>
        <w:tblBorders>
          <w:top w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>
          <w:left w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>
          <w:bottom w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>
          <w:right w:val="single" w:sz="6" w:space="0" w:color="CBD5E1"/>
          <w:insideH w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
          <w:insideV w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
        </w:tblBorders>
      </w:tblPr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="2800" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Nama Berkas Java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2200" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Layer / Komponen</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Tanggung Jawab Utama (Responsibility)</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="2800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>MainActivity.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Presentation / Activity</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Dashboard utama pembacaan, sinkronisasi visual ProgressBar, pelacak streak harian, dan tombol penyelesaian bacaan.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="2800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>SelectCanonActivity.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Onboarding &amp; Routing</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Layar pemilihan kanon Katolik vs Protestan dengan highlight kartu visual, guard routing, dan dialog konfirmasi pergantian kanon.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="2800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>PreferenceHelper.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Data Persistence Helper</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Abstraksi SharedPreferences terpusat untuk menyimpan pasal saat ini, streak, tanggal bacaan terakhir, dan pengaturan target.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="2800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>Kitab.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Domain Model (POJO)</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Struktur objek data kitab murni: nama kitab, jumlah bab/pasal, total ayat, dan klasifikasi perjanjian.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="2800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>KitabData.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>In-Memory Engine &amp; Seeder</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Katalog data lengkap 73/66 kitab beserta algoritma pemetaan urutan pasal global ke kitab dan bab spesifik.</w:t></w:r></w:p></w:tc>
      </w:tr>
    </w:tbl>

    <w:p><w:pPr><w:spacing w:before="240" w:after="100"/></w:pPr></w:p>

    <!-- 1. MAINACTIVITY.JAVA -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="80"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="24"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>1. Pembedahan Mendalam: MainActivity.java</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr>
      <w:r><w:t>MainActivity merupakan pusat interaksi pengguna (User Interface Controller) saat aplikasi berada pada siklus penggunaan rutin. Berkas ini mengendalikan tampilan dashboard pembacaan Alkitab harian.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>A. Komponen UI &amp; Binding:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Menghubungkan elemen layout XML activity_main.xml ke objek Java melalui findViewById, antara lain: tvNamaKitab, tvPasal, tvStreak, progressBar, tvCanonBadge, btnGantiCanon, btnTandaiSelesai, dan btnReset.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>B. Logika Metode loadData():</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr>
    <w:r><w:t>1. Mengambil jenis kanon yang aktif dari SharedPreferences (Katolik atau Protestan).</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr>
    <w:r><w:t>2. Mengatur nilai maksimum ProgressBar secara dinamis melalui KitabData.getTotalPasal(canon) — 1.334 pasal untuk Katolik dan 1.189 pasal untuk Protestan.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr>
    <w:r><w:t>3. Memanggil KitabData.getNamaPasalDariUrutan() untuk menampilkan nama kitab dan nomor bab saat ini.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>4. Menghitung persentase progres dan memeriksa apakah bacaan pada hari kalender saat ini sudah diselesaikan.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>C. Logika Metode selesaikanBacaanHariIni():</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Menaikkan indeks pasal sebanyak 1 angka, menambahkan streak harian +1 hari, dan menyimpan tanggal hari ini (yyyy-MM-dd) ke SharedPreferences. Setelah itu, tombol langsung di-disable dengan label &quot;SUDAH SELESAI HARI INI&quot; untuk mencegah spam klik.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>D. Proteksi Reset Progres (btnReset):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="120"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Menampilkan AlertDialog konfirmasi sebelum data diatur ulang ke Pasal 1, streak 0, dan tanggal kosong, mencegah ketidaksengajaan kehilangan data oleh pengguna.</w:t></w:r>
    </w:p>

    <!-- 2. SELECTCANONACTIVITY.JAVA -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="80"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="24"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>2. Pembedahan Mendalam: SelectCanonActivity.java</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr>
      <w:r><w:t>SelectCanonActivity bertindak sebagai pintu gerbang (Gateway &amp; Onboarding) aplikasi sekaligus penyedia fitur pengubah kanon Alkitab secara fleksibel.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>A. Splash / Guard Routing Lifecycle:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Pada method onCreate(), Activity membaca flag boolean 'first_time_canon_selected'. Jika user sudah pernah memilih kanon sebelumnya dan Intent tidak membawa extra 'is_change_request', maka sistem langsung memanggil bukaMainActivity() dan melakukan finish(), sehingga user tidak terganggu dengan layar pilihan berulang kali.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>B. Interaktivitas MaterialCardView (updateCardSelection):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Mengatur strokeWidth (3dp saat aktif, 1dp saat tidak aktif) serta warna stroke (merah primary #DC2626 vs abu-abu netral #CBD5E1) untuk memberikan feedback visual yang jelas pada kartu yang dipilih.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>C. Integritas Data saat Ganti Versi:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="120"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Jika pengguna beralih tradisi kanon (misal Katolik ke Protestan atau sebaliknya), jumlah pasal dan urutan kitab berubah. Oleh karena itu, sistem memunculkan AlertDialog peringatan dan mereset nilai pasal ke 1 demi menghindari kecacatan pemetaan indeks pembacaan.</w:t></w:r>
    </w:p>

    <!-- 3. PREFERENCEHELPER.JAVA -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="80"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="24"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>3. Pembedahan Mendalam: PreferenceHelper.java</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr>
      <w:r><w:t>PreferenceHelper menerapkan pola arsitektur Data Access Object (DAO) / Helper untuk mengisolasi operasi SharedPreferences Android agar kode Activity tetap bersih (Separation of Concerns).</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>A. Kunci-Kunci Konfigurasi (Key Constants):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Menyediakan konstanta privat terproteksi: KEY_STREAK, KEY_LAST_READ_DATE, KEY_CURRENT_PASAL_INDEX, KEY_CURRENT_AYAT_IN_PASAL, KEY_TOTAL_AYAT_READ, KEY_DAILY_TARGET_COUNT, KEY_TARGET_UNIT, dan KEY_CANON_TYPE.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>B. Integrasi Serialisasi JSON dengan Gson:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Memiliki instance Gson untuk mengonversi riwayat pembacaan (List of ReadingHistory) menjadi format String JSON dan sebaliknya, memungkinkan penyimpanan data kompleks di dalam SharedPreferences tanpa membutuhkan tabel SQLite rumit.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>C. Enkapsulasi Getter &amp; Setter:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="120"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Setiap modifikasi data menggunakan edit().putX().apply() yang berjalan secara asynchronous pada thread latar belakang (background thread), menjaga aplikasi tetap responsif dan bebas dari ANR (Application Not Responding).</w:t></w:r>
    </w:p>

    <!-- 4. KITAB.JAVA -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="80"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="24"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>4. Pembedahan Mendalam: Kitab.java</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr>
      <w:r><w:t>Kitab.java adalah Plain Old Java Object (POJO) yang merepresentasikan model entitas sebuah kitab dalam Alkitab. Berkas ini berfokus murni pada struktur data dan enkapsulasi (OOP Encapsulation).</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>A. Struktur Atribut Imutabel (Final):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr>
    <w:r><w:t>1. private final String nama: Menyimpan nama resmi kitab (misalnya &quot;Kejadian&quot;, &quot;Mazmur&quot;, &quot;Matius&quot;).</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr>
    <w:r><w:t>2. private final int jumlahBab: Total pasal dalam kitab tersebut (misalnya Mazmur memiliki 150 bab).</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr>
    <w:r><w:t>3. private final int totalAyat: Jumlah keseluruhan ayat dalam kitab tersebut untuk kalkulasi detail.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>4. private final String perjanjian: Kategori kanonik (&quot;Perjanjian Lama&quot;, &quot;Perjanjian Baru&quot;, atau &quot;Deuterokanonika&quot;).</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>B. Manfaat Desain Imutabel:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="120"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Penggunaan modifier final dan ketiadaan setter menjamin bahwa data master kitab tidak dapat dimutasi atau dirusak secara sengaja maupun tidak sengaja selama siklus hidup aplikasi berjalan (Thread-Safe).</w:t></w:r>
    </w:p>

    <!-- 5. KITABDATA.JAVA -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="80"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="24"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>5. Pembedahan Mendalam: KitabData.java</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr>
      <w:r><w:t>KitabData merupakan jantung kalkulasi (Engine &amp; In-Memory Repository) aplikasi. Berkas ini menyimpan data statis Alkitab serta menyediakan algoritma pemetaan pasal.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>A. Koleksi Dataset Statis (getDaftarKitab):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Membangun daftar kitab secara dinamis berdasarkan parameter canonType. Jika canonType adalah Katolik, sistem menyisipkan kitab-kitab Deuterokanonika (Tobit, Yudit, 1 &amp; 2 Makabe, Kebijaksanaan Salomo, Sirakh, dan Barukh) sehingga total berjumlah 73 kitab (1.334 pasal). Jika Protestan, hanya 66 kitab (1.189 pasal) yang dimuat.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>B. Algoritma Pemetaan Pasal Global (getNamaPasalDariUrutan):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="60"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Algoritma ini mengiterasi setiap Kitab dalam list sambil mengakumulasi variabel 'hitungan'. Jika urutan pasal yang dicari berada di dalam rentang bab kitab saat itu, nomor bab lokal dihitung dengan rumus: noBab = urutanPasalGlobal - hitungan. Contoh: jika urutan = 52, karena Kejadian memiliki 50 bab, maka sistem langsung mengembalikan hasil &quot;Keluaran 2&quot;.</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>C. Fitur Rentang Bacaan (getRentangBacaan):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="160"/><w:jc w:val="both"/></w:pPr>
    <w:r><w:t>Menghitung teks interval bacaan hari ini, misalnya &quot;Kejadian 1 s/d Kejadian 3&quot; saat pengguna menargetkan 3 pasal per hari, sehingga pengguna memahami batas target bacaannya secara presisi.</w:t></w:r>
    </w:p>

    <!-- LEMBAR PENUTUP -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="CBD5E1"/></w:pBdr>
        <w:spacing w:before="240" w:after="100"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>KESIMPULAN ANALISIS ARSITEKTUR KODE</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="180"/><w:jc w:val="both"/></w:pPr>
      <w:r><w:t>Kelima berkas Java di atas saling berkolaborasi dengan prinsip Separation of Concerns yang solid: Kitab dan KitabData menangani data domain dan algoritma matematis, PreferenceHelper menangani persistensi data lokal, SelectCanonActivity mengatur gerbang onboarding pengguna, dan MainActivity memimpin presentasi visual dashboard yang interaktif dan nyaman digunakan.</w:t></w:r>
    </w:p>

  </w:body>
</w:document>
'@

$out1 = "D:\. Pelajaran\KK\PPB\APK Alkitab\PENJELASAN_KODE_JAVA_LENTERA_KEHIDUPAN.docx"
Create-CleanDocx -outputPath $out1 -documentXmlContent $docXml
Write-Output "SUKSES BUAT DOKUMEN: $out1"

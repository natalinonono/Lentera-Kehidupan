Add-Type -AssemblyName System.IO.Compression.FileSystem

$tempDir = [System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.Guid]::NewGuid().ToString())
New-Item -ItemType Directory -Path $tempDir | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempDir "_rels") | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempDir "word") | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempDir "word\_rels") | Out-Null

# 1. [Content_Types].xml
$contentTypes = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
  <Override PartName="/word/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/>
</Types>
'@
[System.IO.File]::WriteAllText((Join-Path $tempDir "[Content_Types].xml"), $contentTypes, [System.Text.Encoding]::UTF8)

# 2. _rels/.rels
$rootRels = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>
'@
[System.IO.File]::WriteAllText((Join-Path $tempDir "_rels\.rels"), $rootRels, [System.Text.Encoding]::UTF8)

# 3. word/_rels/document.xml.rels
$docRels = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
</Relationships>
'@
[System.IO.File]::WriteAllText((Join-Path $tempDir "word\_rels\document.xml.rels"), $docRels, [System.Text.Encoding]::UTF8)

# 4. word/styles.xml
$stylesXml = @'
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
'@
[System.IO.File]::WriteAllText((Join-Path $tempDir "word\styles.xml"), $stylesXml, [System.Text.Encoding]::UTF8)

# 5. word/document.xml
$docXml = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:body>
    
    <!-- JUDUL LAPORAN UTAMA -->
    <w:p>
      <w:pPr>
        <w:jc w:val="center"/>
        <w:spacing w:before="100" w:after="60"/>
      </w:pPr>
      <w:r>
        <w:rPr>
          <w:rFonts w:ascii="Arial" w:hAnsi="Arial"/>
          <w:b/>
          <w:sz w:val="34"/>
          <w:color w:val="1A202C"/>
        </w:rPr>
        <w:t>LAPORAN PRAKTIKUM &amp; JOBSHEET PROJECT</w:t>
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
          <w:sz w:val="28"/>
          <w:color w:val="DC2626"/>
        </w:rPr>
        <w:t>PENGEMBANGAN APLIKASI &quot;LENTERA KEHIDUPAN&quot;</w:t>
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
        <w:t>Disusun untuk Penilaian Tengah Semester (PTS) - Mata Pelajaran Pemrograman Perangkat Bergerak (PPB)</w:t>
      </w:r>
    </w:p>

    <!-- TABEL IDENTITAS SISWA -->
    <w:tbl>
      <w:tblPr>
        <w:tblW w:w="9360" w:type="dxa"/>
        <w:tblBorders>
          <w:top w:val="single" w:sz="6" w:space="0" w:color="E2E8F0"/>
          <w:left w:val="single" w:sz="6" w:space="0" w:color="E2E8F0"/>
          <w:bottom w:val="single" w:sz="6" w:space="0" w:color="E2E8F0"/>
          <w:right w:val="single" w:sz="6" w:space="0" w:color="E2E8F0"/>
          <w:insideH w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
          <w:insideV w:val="none"/>
        </w:tblBorders>
      </w:tblPr>
      <w:tr>
        <w:tc>
          <w:tcPr><w:tcW w:w="2600" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="F4F5F7"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="20"/></w:rPr><w:t>Nama Siswa</w:t></w:r></w:p>
        </w:tc>
        <w:tc>
          <w:tcPr><w:tcW w:w="6760" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:sz w:val="20"/></w:rPr><w:t>: Natalino / [Isi Nama Lengkap Anda]</w:t></w:r></w:p>
        </w:tc>
      </w:tr>
      <w:tr>
        <w:tc>
          <w:tcPr><w:tcW w:w="2600" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="F4F5F7"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="20"/></w:rPr><w:t>Kelas / Kompetensi Keahlian</w:t></w:r></w:p>
        </w:tc>
        <w:tc>
          <w:tcPr><w:tcW w:w="6760" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:sz w:val="20"/></w:rPr><w:t>: XI / XII - Rekayasa Perangkat Lunak (RPL)</w:t></w:r></w:p>
        </w:tc>
      </w:tr>
      <w:tr>
        <w:tc>
          <w:tcPr><w:tcW w:w="2600" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="F4F5F7"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="20"/></w:rPr><w:t>Nama Aplikasi</w:t></w:r></w:p>
        </w:tc>
        <w:tc>
          <w:tcPr><w:tcW w:w="6760" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="20"/><w:color w:val="DC2626"/></w:rPr><w:t>: Lentera Kehidupan (Bible Reading Progress Tracker)</w:t></w:r></w:p>
        </w:tc>
      </w:tr>
      <w:tr>
        <w:tc>
          <w:tcPr><w:tcW w:w="2600" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="F4F5F7"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="20"/></w:rPr><w:t>Package Name</w:t></w:r></w:p>
        </w:tc>
        <w:tc>
          <w:tcPr><w:tcW w:w="6760" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:sz w:val="20"/></w:rPr><w:t>: com.smk.lenterakehidupan</w:t></w:r></w:p>
        </w:tc>
      </w:tr>
      <w:tr>
        <w:tc>
          <w:tcPr><w:tcW w:w="2600" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="F4F5F7"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="20"/></w:rPr><w:t>Lingkungan Kerja (IDE)</w:t></w:r></w:p>
        </w:tc>
        <w:tc>
          <w:tcPr><w:tcW w:w="6760" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:sz w:val="20"/></w:rPr><w:t>: Android Studio Iguana / Hedgehog / Koala | Gradle 8.x | Java 8</w:t></w:r></w:p>
        </w:tc>
      </w:tr>
    </w:tbl>

    <w:p><w:pPr><w:spacing w:before="240" w:after="80"/></w:pPr></w:p>

    <!-- BAB I -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="100"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="26"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>BAB I. PENDAHULUAN &amp; TUJUAN PROYEK</w:t>
      </w:r>
    </w:p>

    <w:p>
      <w:pPr><w:spacing w:after="80"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="2D3748"/></w:rPr><w:t>1.1 Latar Belakang</w:t></w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="120"/><w:jc w:val="both"/></w:pPr>
      <w:r>
        <w:t>Membaca seluruh isi Alkitab merupakan komitmen rohani penting, namun sering menghadapi kendala seperti lupa pasal terakhir, hilangnya motivasi harian, serta perbedaan tradisi kanon antara Katolik (73 Kitab / 1.334 Pasal) dan Protestan (66 Kitab / 1.189 Pasal). Aplikasi &quot;Lentera Kehidupan&quot; hadir sebagai solusi aplikasi mobile native Android berbasis Java yang menyajikan alur onboarding pemilihan kanon elegan (SelectCanonActivity), pencatatan progres persisten via SharedPreferences, streak counter harian, dan antarmuka flat modern.</w:t>
      </w:r>
    </w:p>

    <w:p>
      <w:pPr><w:spacing w:after="80"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="2D3748"/></w:rPr><w:t>1.2 Tujuan Pembelajaran</w:t></w:r>
    </w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>1. Memahami arsitektur multi-activity (SelectCanonActivity dan MainActivity) menggunakan Android Intent.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>2. Mengimplementasikan antarmuka seleksi kanon interaktif menggunakan MaterialCardView dengan stroke highlight dinamis.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>3. Menerapkan penyimpanan data lokal persisten SharedPreferences (kanon, pasal, streak, tanggal bacaan terakhir).</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>4. Membangun logika pemetaan pasal global (1 s/d 1334) ke nama kitab dan nomor pasalnya secara dinamis.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="160"/></w:pPr><w:r><w:t>5. Menguji fungsionalitas dan konsistensi data aplikasi secara komprehensif.</w:t></w:r></w:p>

    <!-- BAB II -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="100"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="26"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>BAB II. BEDAH WORKSPACE ANDROID STUDIO &amp; FITUR APLIKASI</w:t>
      </w:r>
    </w:p>

    <w:p>
      <w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr>
      <w:r>
        <w:t>Membedah struktur berkas proyek di panel Project View (Android Mode) pada Android Studio:</w:t>
      </w:r>
    </w:p>

    <!-- TABEL STRUKTUR DIREKTORI -->
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
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>File / Lokasi Folder</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Peran Arsitektur</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Fungsi &amp; Hubungan Terhadap Fitur</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>manifests/AndroidManifest.xml</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>App Manifest</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Mendaftarkan SelectCanonActivity sebagai MAIN &amp; LAUNCHER (gerbang awal aplikasi) dan MainActivity sebagai Activity utama pembacaan.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>java/.../activity/SelectCanonActivity.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Onboarding &amp; Canon Switcher</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Menangani interaksi kartu Katolik/Protestan, routing langsung ke MainActivity jika sudah pernah memilih, dan dialog konfirmasi ganti kanon.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>java/.../activity/MainActivity.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Main Dashboard Controller</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Menampilkan bacaan hari ini, badge kanon aktif, tombol 'Ganti', pencatatan streak, tombol selesai harian, dan dialog reset data.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>java/.../model/KitabData.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Data Repository &amp; Engine</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Database statis berisi seluruh kitab Katolik (73 kitab / 1334 pasal) dan Protestan (66 kitab / 1189 pasal) beserta fungsi pemetaan pasalnya.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>res/layout/activity_select_canon.xml</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Layout Pemilihan Kanon</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Tampilan landing card MaterialCardView bersudut flat (0dp) dengan stroke highlight dan tombol Mulai Membaca.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>res/layout/activity_main.xml</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Layout Dashboard Utama</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Tampilan bacaan saat ini, urutan pasal, streak counter, progress bar horizontal, badge kanon terpilih, tombol selesai dan reset.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>java/.../helper/PreferenceHelper.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Storage Manager</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Enkapsulasi SharedPreferences untuk membaca dan menyimpan key pasal, streak, histori tanggal, dan unit target bacaan pengguna.</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>java/.../model/Kitab.java</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>POJO Model</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Objek data entitas representasi struktur kitab (nama, kategori perjanjian, total pasal, ayat, dan status checklist).</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="3200" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/></w:rPr><w:t>res/layout/dialog_target_setup.xml</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Custom Dialog Layout</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4360" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Form antarmuka dialog interaktif untuk mengatur komitmen target harian pengguna (satuan pasal atau ayat per hari).</w:t></w:r></w:p></w:tc>
      </w:tr>
    </w:tbl>

    <w:p><w:pPr><w:spacing w:before="180" w:after="80"/></w:pPr></w:p>

    <!-- BEDAH FITUR & WORKSPACE MENDALAM -->
    <w:p><w:pPr><w:spacing w:after="80"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="2D3748"/></w:rPr><w:t>2.2 Pembedahan Fitur &amp; Logika Workspace Android Studio</w:t></w:r></w:p>
    
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>1. Modul Onboarding &amp; Dedicated Canon Selection (SelectCanonActivity):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr><w:r><w:t>Diatur sebagai LAUNCHER di AndroidManifest.xml. Ketika aplikasi dibuka pertama kali, sistem memeriksa kunci Boolean 'first_time_canon_selected'. Jika bernilai false, layar pemilihan ditampilkan dengan dua MaterialCardView interaktif: Katolik (73 Kitab) dan Protestan (66 Kitab). Kartu yang dipilih akan memicu perubahan ketebalan stroke serta warna aksen secara dinamis. Bila sudah pernah memilih, Activity ini langsung mengalihkan rute ke MainActivity via Intent tanpa delay.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>2. Fitur Ganti Kanon dengan Proteksi Konfirmasi (Dynamic Canon Switcher):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr><w:r><w:t>Pada dashboard utama, pengguna dapat mengklik tombol teks '(Ganti)' pada badge kanon. Sistem memanggil SelectCanonActivity dengan parameter intent 'is_change_request = true'. Jika pengguna memilih versi berbeda, sistem menampilkan AlertDialog konfirmasi yang memperingatkan bahwa progres membaca akan di-reset ke Pasal 1 untuk mencegah ketidaksinkronan data indeks pasal.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>3. Pelacak Progres &amp; Engine Pemetaan Kitab Dinamis (KitabData.java):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr><w:r><w:t>Tanpa memerlukan koneksi server atau database SQLite yang rumit, KitabData bertindak sebagai In-Memory Data Repository. Fungsi getNamaPasalDariUrutan(canon, urutan) secara cerdas mengiterasi daftar kitab dan jumlah pasalnya. Saat urutan pasal mencapai angka tertentu (misal pasal ke-51), sistem otomatis memetakan bahwa pembacaan telah memasuki Kitab Keluaran Pasal 1.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>4. Daily Reading Streak &amp; Pencegahan Klik Berulang (Activity Lifecycle &amp; State):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr><w:r><w:t>Sistem mencatat tanggal kalender (format yyyy-MM-dd) di SharedPreferences saat tombol 'TANDAI SELESAI HARI INI' diklik. Jika tanggal hari ini sama dengan tanggal bacaan terakhir, tombol langsung berstatus disabled dengan teks 'SUDAH SELESAI HARI INI'. Ini menjaga kedisiplinan membaca satu komitmen harian dan menghentikan manipulasi penambahan streak berulang kali.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>5. Industrial Minimalist Flat UI Aesthetic:</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="80"/><w:jc w:val="both"/></w:pPr><w:r><w:t>Arsitektur UI dirancang menggunakan pedoman Material Design dengan sudut flat tegas (cornerSize 0dp), elevasi 0dp, palet warna sejuk slate (#F4F5F7, #1A202C) serta aksen merah menyala (#DC2626) yang menghasilkan tampilan modern, rapi, dan memiliki tingkat keterbacaan tinggi saat diuji pada layar ponsel.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:color w:val="DC2626"/></w:rPr><w:t>6. Manajemen Data Persisten (PreferenceHelper &amp; SharedPreferences):</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="160"/><w:jc w:val="both"/></w:pPr><w:r><w:t>Semua status pembacaan, jumlah pasal, capaian streak, konfigurasi target, dan histori waktu disimpan secara persisten di penyimpanan internal aplikasi dalam format XML Key-Value terproteksi (MODE_PRIVATE), sehingga data tetap aman dan tersimpan meskipun aplikasi ditutup paksa atau smartphone di-reboot.</w:t></w:r></w:p>

    <!-- BAB III -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="100"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="26"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>BAB III. LANGKAH-LANGKAH PROSES MENGERJAKAN PROJECT (JOBSHEET)</w:t>
      </w:r>
    </w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr><w:t>Langkah 1: Setup Lingkungan &amp; Pembuatan Proyek di Android Studio</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>1. Buka Android Studio, pilih New Project -&gt; Phone and Tablet -&gt; Template 'Empty Views Activity'.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>2. Konfigurasikan Name: Lentera Kehidupan, Package: com.smk.lenterakehidupan, Language: Java, Minimum SDK: API 24 (Android 7.0 Nougat).</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="100"/></w:pPr><w:r><w:t>3. Tunggu proses Gradle Build dan sinkronisasi dependensi selesai tanpa error.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr><w:t>Langkah 2: Konfigurasi Dependensi, Warna, &amp; Gaya Flat</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>1. Buka build.gradle.kts (Module: app) dan pastikan dependensi Material Components dan Gson terdaftar.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>2. Buka res/values/colors.xml, definisikan palet: slate dark (#1A202C), slate grey (#2D3748), soft grey background (#F4F5F7), dan primary red (#DC2626).</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>3. Buka res/values/themes.xml, terapkan Theme.MaterialComponents.Light.NoActionBar dan set cornerSize 0dp untuk gaya industrial flat.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="100"/></w:pPr><w:r><w:t>4. Sesuaikan nama aplikasi di res/values/strings.xml menjadi 'Lentera Kehidupan'.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr><w:t>Langkah 3: Pembuatan Model Data &amp; Repositori Kitab</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>1. Buat package model di app/src/main/java/com/smk/lenterakehidupan/model/.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>2. Buat class Kitab.java untuk menampung properti id, nama kitab, kategori (Perjanjian Lama/Baru/Deutero), dan jumlah pasal.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="100"/></w:pPr><w:r><w:t>3. Buat class KitabData.java yang memuat list statis lengkap 73 kitab Katolik (1.334 pasal) dan 66 kitab Protestan (1.189 pasal) serta method getNamaPasalDariUrutan().</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr><w:t>Langkah 4: Pembuatan Layar Onboarding Seleksi Kanon (SelectCanonActivity)</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>1. Buat layout res/layout/activity_select_canon.xml dengan 2 MaterialCardView interaktif berkontras tinggi dan tombol Mulai Membaca.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>2. Buat SelectCanonActivity.java: tangani listener klik kartu, update stroke kartu terpilih, validasi apakah user sedang ganti kanon, dan simpan pilihan ke SharedPreferences.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="100"/></w:pPr><w:r><w:t>3. Konfigurasikan AndroidManifest.xml untuk menjadikan SelectCanonActivity sebagai MAIN dan LAUNCHER.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr><w:t>Langkah 5: Pembuatan Layar Utama Dashboard (MainActivity)</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>1. Buat layout res/layout/activity_main.xml dengan Header Info Kitab, Nomor Pasal besar, Streak Counter, ProgressBar, Badge Kanon aktif, tombol 'Ganti', serta tombol selesai dan reset.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:t>2. Tulis logika di MainActivity.java untuk method loadData(), event klik btnTandaiSelesai, dan navigasi balik ke SelectCanonActivity dengan parameter is_change_request.</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="100"/></w:pPr><w:r><w:t>3. Tambahkan dialog konfirmasi AlertDialog pada tombol Reset Progres.</w:t></w:r></w:p>

    <w:p><w:pPr><w:spacing w:after="40"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val="22"/><w:color w:val="1A202C"/></w:rPr><w:t>Langkah 6: Pengujian Aplikasi (Run &amp; Debugging)</w:t></w:r></w:p>
    <w:p><w:pPr><w:spacing w:after="160"/></w:pPr><w:r><w:t>Jalankan aplikasi di emulator atau perangkat Android fisik, uji perpindahan Activity, verifikasi data SharedPreferences, uji fitur ganti kanon dan tombol selesaikan bacaan harian.</w:t></w:r></w:p>

    <!-- BAB IV -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="100"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="26"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>BAB IV. HASIL PENGUJIAN &amp; MATRIKS EVALUASI SISTEM</w:t>
      </w:r>
    </w:p>

    <!-- TABEL HASIL UJI -->
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
        <w:tc><w:tcPr><w:tcW w:w="800" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>No</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2600" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Skenario Uji</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2760" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Hasil yang Diharapkan</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Hasil Pengamatan</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/><w:shd w:val="clear" w:color="auto" w:fill="2D3748"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="FFFFFF"/></w:rPr><w:t>Status</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>1</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2600" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Peluncuran pertama (First Launch)</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2760" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Membuka SelectCanonActivity untuk memilih Katolik atau Protestan.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Tampil layar pemilihan kanon secara tepat.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="059669"/></w:rPr><w:t>VALID (PASS)</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>2</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2600" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Peluncuran berikutnya (Subsequent Launch)</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2760" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Langsung masuk ke MainActivity tanpa menampilkan halaman onboarding lagi.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Sistem mendeteksi KEY_FIRST_TIME dan langsung berpindah ke dashboard.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="059669"/></w:rPr><w:t>VALID (PASS)</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>3</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2600" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Menekan tombol Selesai Membaca</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2760" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Pasal bertambah 1, Streak +1 Hari, tombol disabled.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Data tersimpan di SharedPreferences secara persisten.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="059669"/></w:rPr><w:t>VALID (PASS)</w:t></w:r></w:p></w:tc>
      </w:tr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>4</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2600" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Menekan tombol '(Ganti)' Kanon</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="2760" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Membuka kembali SelectCanonActivity dengan flag is_change_request.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1800" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:sz w:val="18"/></w:rPr><w:t>Layar pemilihan terbuka dan dialog peringatan muncul jika versi diubah.</w:t></w:r></w:p></w:tc>
        <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/></w:tcPr><w:p><w:r><w:rPr><w:b/><w:sz w:val="18"/><w:color w:val="059669"/></w:rPr><w:t>VALID (PASS)</w:t></w:r></w:p></w:tc>
      </w:tr>
    </w:tbl>

    <w:p><w:pPr><w:spacing w:before="180" w:after="80"/></w:pPr></w:p>

    <!-- BAB V -->
    <w:p>
      <w:pPr>
        <w:pBdr><w:bottom w:val="single" w:sz="12" w:space="4" w:color="DC2626"/></w:pBdr>
        <w:spacing w:before="240" w:after="100"/>
      </w:pPr>
      <w:r>
        <w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="26"/><w:color w:val="1A202C"/></w:rPr>
        <w:t>BAB V. KESIMPULAN &amp; LEMBAR PENGESAHAN</w:t>
      </w:r>
    </w:p>

    <w:p>
      <w:pPr><w:spacing w:after="120"/><w:jc w:val="both"/></w:pPr>
      <w:r>
        <w:t>Aplikasi &quot;Lentera Kehidupan&quot; berhasil dibangun dengan arsitektur native Android yang kokoh, memisahkan logika UI, alur onboarding pemilihan kanon Alkitab, dan pemetaan pasal otomatis secara offline dan handal.</w:t>
      </w:r>
    </w:p>

    <!-- LEMBAR PENGESAHAN -->
    <w:p><w:pPr><w:spacing w:before="300" w:after="60"/></w:pPr></w:p>
    <w:tbl>
      <w:tblPr>
        <w:tblW w:w="9360" w:type="dxa"/>
        <w:tblBorders>
          <w:top w:val="none"/><w:left w:val="none"/><w:bottom w:val="none"/><w:right w:val="none"/>
          <w:insideH w:val="none"/><w:insideV w:val="none"/>
        </w:tblBorders>
      </w:tblPr>
      <w:tr>
        <w:tc><w:tcPr><w:tcW w:w="4680" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:t>Mengetahui,</w:t></w:r></w:p>
          <w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:rPr><w:b/></w:rPr><w:t>Guru Pengampu Mapel PPB</w:t></w:r></w:p>
          <w:p><w:pPr><w:spacing w:before="700"/><w:jc w:val="center"/></w:pPr><w:r><w:t>( ___________________________ )</w:t></w:r></w:p>
          <w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:t>NIP. </w:t></w:r></w:p>
        </w:tc>
        <w:tc><w:tcPr><w:tcW w:w="4680" w:type="dxa"/></w:tcPr>
          <w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:t>Siswa Pembuat Project,</w:t></w:r></w:p>
          <w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:rPr><w:b/></w:rPr><w:t>Praktikan / Pengembang</w:t></w:r></w:p>
          <w:p><w:pPr><w:spacing w:before="700"/><w:jc w:val="center"/></w:pPr><w:r><w:t>( Natalino )</w:t></w:r></w:p>
          <w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:t>NIS / NISN. </w:t></w:r></w:p>
        </w:tc>
      </w:tr>
    </w:tbl>

  </w:body>
</w:document>
'@
[System.IO.File]::WriteAllText((Join-Path $tempDir "word\document.xml"), $docXml, [System.Text.Encoding]::UTF8)

# Output target
$outputDocx = "D:\. Pelajaran\KK\PPB\APK Alkitab\LAPORAN_JOBSHEET_PTS_LENTERA_KEHIDUPAN.docx"
if ([System.IO.File]::Exists($outputDocx)) {
    [System.IO.File]::Delete($outputDocx)
}

[System.IO.Compression.ZipFile]::CreateFromDirectory($tempDir, $outputDocx)
Remove-Item -Recurse -Force $tempDir

Write-Output "SUKSES: Dokumen Word berhasil dibuat di: $outputDocx"

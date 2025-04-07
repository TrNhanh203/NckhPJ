package com.example.facilitiesmanagementpj.ui.screen.admin
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.facilitiesmanagementpj.data.relation.PhanCongThongTin
import com.example.facilitiesmanagementpj.data.relation.ThietBiDaSua
import com.example.facilitiesmanagementpj.ui.component.SignaturePad
import java.text.SimpleDateFormat
import java.util.Date

import android.graphics.Paint
import android.os.Environment

import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale


// Composable màn hình nghiệm thu có chữ ký điện tử (dữ liệu fake để test preview)
// Composable màn hình nghiệm thu có chữ ký điện tử – 3 tab: preview, ký A, ký B
@Composable
fun BienBanNghiemThuScreen(
    onHoanTat: (Bitmap, Bitmap) -> Unit
) {
    val tabTitles = listOf("Xem trước", "Ký bên A", "Ký bên B")
    var selectedTabIndex by remember { mutableStateOf(0) }

    var kyBenA by remember { mutableStateOf<Bitmap?>(null) }
    var kyBenB by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val thongTinPhanCong = PhanCongThongTin(
        tenBenA = "Trần Thị Admin",
        donViBenA = "Phòng Hành chính",
        tenBenB = "Nguyễn Văn Kỹ",
        viTri = "P.203 – Khoa CNTT",
        danhSachThietBi = listOf(
            ThietBiDaSua("Máy in HP 1020", "P.203", "Thay hộp mực, vệ sinh"),
            ThietBiDaSua("Máy chiếu Epson X500", "P.205", "Kiểm tra nguồn và ống kính")
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().padding(bottom = 72.dp)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    // Tab Preview
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text("BIÊN BẢN NGHIỆM THU SỬA CHỮA, BẢO DƯỠNG THIẾT BỊ", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Text("Ngày: ${SimpleDateFormat("dd/MM/yyyy").format(Date())}")
                        Text("Địa điểm: ${thongTinPhanCong.viTri}")

                        Spacer(Modifier.height(16.dp))
                        Text("BÊN A – Đơn vị sở hữu thiết bị:", fontWeight = FontWeight.Bold)
                        Text("Họ tên: ${thongTinPhanCong.tenBenA}")
                        Text("Đơn vị: ${thongTinPhanCong.donViBenA}")

                        Spacer(Modifier.height(12.dp))
                        Text("BÊN B – Kỹ thuật viên thực hiện:", fontWeight = FontWeight.Bold)
                        Text("Họ tên: ${thongTinPhanCong.tenBenB}")
                        Text("Đơn vị: Phòng Kỹ thuật")

                        Spacer(Modifier.height(16.dp))
                        Text("Danh sách thiết bị đã xử lý:", fontWeight = FontWeight.SemiBold)
                        thongTinPhanCong.danhSachThietBi.forEachIndexed { index, item ->
                            Text("${index + 1}. ${item.tenThietBi} – ${item.viTri}: ${item.noiDung}")
                        }

                        Spacer(Modifier.height(16.dp))
                        Text("KẾT LUẬN: Thiết bị đã hoạt động bình thường sau bảo trì.")
                        Spacer(Modifier.height(16.dp))

                        if (kyBenA != null || kyBenB != null) {
                            Spacer(Modifier.height(24.dp))
                            Text("Chữ ký xác nhận:", fontWeight = FontWeight.SemiBold)

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Bên A", fontWeight = FontWeight.Medium)
                                    kyBenA?.let {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = "Chữ ký bên A",
                                            modifier = Modifier
                                                .size(150.dp)
                                                .border(1.dp, Color.Gray)
                                        )
                                    }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Bên B", fontWeight = FontWeight.Medium)
                                    kyBenB?.let {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = "Chữ ký bên B",
                                            modifier = Modifier
                                                .size(150.dp)
                                                .border(1.dp, Color.Gray)
                                        )
                                    }
                                }
                            }
                        }

                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("BÊN A ký xác nhận", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(12.dp))
                        SignaturePad(
                            modifier = Modifier
                                .width(300.dp)
                                .height(180.dp),
                            onSigned = { kyBenA = it }
                        )
                    }
                }

                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("BÊN B ký xác nhận", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(12.dp))
                        SignaturePad(
                            modifier = Modifier
                                .width(300.dp)
                                .height(180.dp),
                            onSigned = { kyBenB = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }

        Button(
            onClick = {
                if (kyBenA != null && kyBenB != null) {
                    exportBienBanToPdf(context, thongTinPhanCong, kyBenA!!, kyBenB!!)
                } else {
                    Toast.makeText(context, "Vui lòng ký đầy đủ cả hai bên", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Done, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tạo bản hoàn chỉnh")
        }
    }





}


fun exportBienBanToPdf(
    context: Context,
    thongTin: PhanCongThongTin,
    kyA: Bitmap,
    kyB: Bitmap
) {
    val pdf = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdf.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint().apply {
        color = android.graphics.Color.BLACK
        isAntiAlias = true
    }

    var y = 50f
    val centerX = canvas.width / 2f
    val marginX = 40f
    val maxTextWidth = canvas.width - 2 * marginX

    fun drawCenter(text: String, textSize: Float = 14f, bold: Boolean = false) {
        paint.textSize = textSize
        paint.isFakeBoldText = bold
        val x = (canvas.width - paint.measureText(text)) / 2f
        canvas.drawText(text, x, y, paint)
        y += 26f
    }

    fun drawWrappedText(text: String, startX: Float, maxWidth: Float, lineSpacing: Float) {
        var remainingText = text
        while (remainingText.isNotEmpty()) {
            val count = paint.breakText(remainingText, true, maxWidth, null)
            val line = remainingText.substring(0, count)
            canvas.drawText(line, startX, y, paint)
            remainingText = remainingText.substring(count)
            y += lineSpacing
        }
    }

    fun drawLeft(text: String, textSize: Float = 12f, bold: Boolean = false) {
        paint.textSize = textSize
        paint.isFakeBoldText = bold
        drawWrappedText(text, marginX, maxTextWidth, lineSpacing = 22f)
    }



    // ===== Header =====
    drawCenter("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", textSize = 14f, bold = true)
    drawCenter("Độc lập – Tự do – Hạnh phúc", textSize = 14f, bold = true)
    y += 12f
    drawCenter("BIÊN BẢN NGHIỆM THU", textSize = 16f, bold = true)
    drawCenter("v/v: bảo trì, sửa chữa thiết bị", textSize = 14f)
    y += 12f

    // ===== Nội dung =====
    drawLeft("Căn cứ Giấy đề xuất ngày ... tháng ... năm ... của PHÒNG HÀNH CHÍNH về việc sửa chữa máy móc thiết bị.")
    y += 8f
    drawLeft("Hôm nay, ngày ... tháng ... năm ..., chúng tôi gồm có:")
    drawLeft("Ông/Bà: TRẦN THỊ ADMIN    Chức vụ: Quản trị viên – Phòng Hành chính")
    drawLeft("Ông/Bà: NGUYỄN VĂN KỸ     Chức vụ: Kỹ thuật viên – Phòng Kỹ thuật")
    y += 8f
    drawLeft("Chúng tôi thống nhất nghiệm thu các thiết bị đã sửa chữa như sau:")
    drawLeft("1. Máy in HP 1020 – P.203: Thay hộp mực, vệ sinh")
    drawLeft("2. Máy chiếu Epson X500 – P.205: Kiểm tra nguồn và ống kính")
    y += 8f
    drawLeft("Chúng tôi xác nhận các thiết bị trên đã hoạt động tốt sau khi sửa chữa, bảo trì.")
    y += 8f
    drawLeft("Biên bản gồm 01 trang, lập thành 02 bản, mỗi bên giữ một bản và có giá trị pháp lý như nhau.")
    y += 40f

    // ===== Chữ ký =====
    paint.textSize = 12f
    paint.isFakeBoldText = true
    canvas.drawText("ĐD Đơn vị sử dụng thiết bị", marginX, y, paint)
    canvas.drawText("ĐD Phòng Kỹ thuật", marginX + 320f, y, paint)

    paint.isFakeBoldText = false
    canvas.drawText("(Ký, ghi rõ họ tên)", marginX + 5f, y + 18f, paint)
    canvas.drawText("(Ký, ghi rõ họ tên)", marginX + 325f, y + 18f, paint)

    // ===== Ảnh chữ ký =====
    val sigSize = 100
    canvas.drawBitmap(kyA.scale(sigSize, sigSize, false), marginX + 10f, y + 40f, null)
    canvas.drawBitmap(kyB.scale(sigSize, sigSize, false), marginX + 330f, y + 40f, null)

    pdf.finishPage(page)

    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "bien_ban_nghiem_thu_mau_chuan_fix.pdf"
    )
    pdf.writeTo(FileOutputStream(file))
    pdf.close()

    Toast.makeText(context, "✅ Đã tạo PDF mẫu đẹp tại:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
}





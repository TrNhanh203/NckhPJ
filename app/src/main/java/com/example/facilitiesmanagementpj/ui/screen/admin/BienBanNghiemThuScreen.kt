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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.BienBanViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel.BienBanViewModel.ThongTinNghiemThu
import java.text.Normalizer
import java.util.Locale
import kotlin.text.compareTo


// Composable màn hình nghiệm thu có chữ ký điện tử (dữ liệu fake để test preview)
// Composable màn hình nghiệm thu có chữ ký điện tử – 3 tab: preview, ký A, ký B
//@Composable
//fun BienBanNghiemThuScreen(
//    onHoanTat: (Bitmap, Bitmap) -> Unit
//) {
//    val tabTitles = listOf("Xem trước", "Ký bên A", "Ký bên B")
//    var selectedTabIndex by remember { mutableStateOf(0) }
//
//    var kyBenA by remember { mutableStateOf<Bitmap?>(null) }
//    var kyBenB by remember { mutableStateOf<Bitmap?>(null) }
//
//    val context = LocalContext.current
//
//    val thongTinPhanCong = PhanCongThongTin(
//        tenBenA = "Trần Thị Admin",
//        donViBenA = "Phòng Hành chính",
//        tenBenB = "Nguyễn Văn Kỹ",
//        viTri = "P.203 – Khoa CNTT",
//        danhSachThietBi = listOf(
//            ThietBiDaSua("Máy in HP 1020", "P.203", "Thay hộp mực, vệ sinh"),
//            ThietBiDaSua("Máy chiếu Epson X500", "P.205", "Kiểm tra nguồn và ống kính")
//        )
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        Column(modifier = Modifier.fillMaxSize().padding(bottom = 72.dp)) {
//            TabRow(selectedTabIndex = selectedTabIndex) {
//                tabTitles.forEachIndexed { index, title ->
//                    Tab(
//                        selected = selectedTabIndex == index,
//                        onClick = { selectedTabIndex = index },
//                        text = { Text(title) }
//                    )
//                }
//            }
//
//            when (selectedTabIndex) {
//                0 -> {
//                    // Tab Preview
//                    Column(
//                        modifier = Modifier
//                            .verticalScroll(rememberScrollState())
//                            .padding(16.dp)
//                    ) {
//                        Text("BIÊN BẢN NGHIỆM THU SỬA CHỮA, BẢO DƯỠNG THIẾT BỊ", style = MaterialTheme.typography.titleLarge)
//                        Spacer(Modifier.height(8.dp))
//                        Text("Ngày: ${SimpleDateFormat("dd/MM/yyyy").format(Date())}")
//                        Text("Địa điểm: ${thongTinPhanCong.viTri}")
//
//                        Spacer(Modifier.height(16.dp))
//                        Text("BÊN A – Đơn vị sở hữu thiết bị:", fontWeight = FontWeight.Bold)
//                        Text("Họ tên: ${thongTinPhanCong.tenBenA}")
//                        Text("Đơn vị: ${thongTinPhanCong.donViBenA}")
//
//                        Spacer(Modifier.height(12.dp))
//                        Text("BÊN B – Kỹ thuật viên thực hiện:", fontWeight = FontWeight.Bold)
//                        Text("Họ tên: ${thongTinPhanCong.tenBenB}")
//                        Text("Đơn vị: Phòng Kỹ thuật")
//
//                        Spacer(Modifier.height(16.dp))
//                        Text("Danh sách thiết bị đã xử lý:", fontWeight = FontWeight.SemiBold)
//                        thongTinPhanCong.danhSachThietBi.forEachIndexed { index, item ->
//                            Text("${index + 1}. ${item.tenThietBi} – ${item.viTri}: ${item.noiDung}")
//                        }
//
//                        Spacer(Modifier.height(16.dp))
//                        Text("KẾT LUẬN: Thiết bị đã hoạt động bình thường sau bảo trì.")
//                        Spacer(Modifier.height(16.dp))
//
//                        if (kyBenA != null || kyBenB != null) {
//                            Spacer(Modifier.height(24.dp))
//                            Text("Chữ ký xác nhận:", fontWeight = FontWeight.SemiBold)
//
//                            Row(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .padding(top = 12.dp),
//                                horizontalArrangement = Arrangement.SpaceEvenly
//                            ) {
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Text("Bên A", fontWeight = FontWeight.Medium)
//                                    kyBenA?.let {
//                                        Image(
//                                            bitmap = it.asImageBitmap(),
//                                            contentDescription = "Chữ ký bên A",
//                                            modifier = Modifier
//                                                .size(150.dp)
//                                                .border(1.dp, Color.Gray)
//                                        )
//                                    }
//                                }
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Text("Bên B", fontWeight = FontWeight.Medium)
//                                    kyBenB?.let {
//                                        Image(
//                                            bitmap = it.asImageBitmap(),
//                                            contentDescription = "Chữ ký bên B",
//                                            modifier = Modifier
//                                                .size(150.dp)
//                                                .border(1.dp, Color.Gray)
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                }
//
//                1 -> {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text("BÊN A ký xác nhận", fontWeight = FontWeight.Medium)
//                        Spacer(Modifier.height(12.dp))
//                        SignaturePad(
//                            modifier = Modifier
//                                .width(300.dp)
//                                .height(180.dp),
//                            onSigned = { kyBenA = it }
//                        )
//                    }
//                }
//
//                2 -> {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text("BÊN B ký xác nhận", fontWeight = FontWeight.Medium)
//                        Spacer(Modifier.height(12.dp))
//                        SignaturePad(
//                            modifier = Modifier
//                                .width(300.dp)
//                                .height(180.dp),
//                            onSigned = { kyBenB = it }
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//        }
//
//        Button(
//            onClick = {
//                if (kyBenA != null && kyBenB != null) {
//                    exportBienBanToPdf(context, thongTinPhanCong, kyBenA!!, kyBenB!!)
//                } else {
//                    Toast.makeText(context, "Vui lòng ký đầy đủ cả hai bên", Toast.LENGTH_SHORT).show()
//                }
//            },
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            Icon(Icons.Default.Done, contentDescription = null)
//            Spacer(Modifier.width(8.dp))
//            Text("Tạo bản hoàn chỉnh")
//        }
//    }
//
//
//
//
//
//}
@Composable
fun BienBanNghiemThuScreen(
    viewModel: BienBanViewModel = hiltViewModel(),
    yeuCauId: Int,
    nguoiXacNhanId: Int,
    navController: NavController,
    onHoanTat: (Bitmap, Bitmap) -> Unit
) {
    val tabTitles = listOf("Xem trước", "Ký bên A", "Ký bên B")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var kyBenA by remember { mutableStateOf<Bitmap?>(null) }
    var kyBenB by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val thongTin = viewModel.thongTinNghiemThu

    LaunchedEffect(Unit) {
        viewModel.loadBienBan(yeuCauId, nguoiXacNhanId)
    }

    ScaffoldLayout(
        title = "Xem trước biên bản",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { innerPadding ->
        if (thongTin == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@ScaffoldLayout
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(innerPadding)
                .padding(bottom = 80.dp)
        )
        {
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
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            "BIÊN BẢN NGHIỆM THU SỬA CHỮA, BẢO DƯỠNG THIẾT BỊ",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Ngày: ${SimpleDateFormat("dd/MM/yyyy").format(Date())}")
                        Text("Địa điểm: Trường Đại Học Thủ Dầu Một") // nếu cần bạn có thể bổ sung vị trí ở đây

                        Spacer(Modifier.height(16.dp))
                        Text(
                            "BÊN A – Đơn vị chịu trách nhiệm thiết bị:",
                            fontWeight = FontWeight.Bold
                        )
                        Text("Họ tên: ${thongTin.tenBenA}")
                        Text("Đơn vị: ${thongTin.donViBenA}")

                        Spacer(Modifier.height(12.dp))
                        Text(
                            "BÊN B – Thực hiện xử lý yêu cầu:",
                            fontWeight = FontWeight.Bold
                        )
                        Text("Họ tên: ${thongTin.tenBenB}")
                        Text("Đơn vị: Phòng Kỹ thuật")

                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Danh sách thiết bị đã xử lý:",
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            // 🔷 HEADER
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    "#",
                                    Modifier
                                        .weight(0.2f)
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                VerticalDivider()
                                Text(
                                    "Tên thiết bị",
                                    Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                VerticalDivider()
                                Text(
                                    "Vị trí",
                                    Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                VerticalDivider()
                                Text(
                                    "Nội dung",
                                    Modifier
                                        .weight(1.2f)
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Divider(color = MaterialTheme.colorScheme.outline)

                            // DÒNG DỮ LIỆU
                            thongTin.danhSachThietBi.forEachIndexed { index, item ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        "${index + 1}",
                                        Modifier
                                            .weight(0.2f)
                                            .align(Alignment.CenterVertically)
                                    )
                                    VerticalDivider()
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        val annotatedText = buildAnnotatedString {
                                            append(item.tenThietBi)
                                            appendInlineContent("iconXemAnh", "[icon]")
                                        }

                                        val inlineContent = mapOf(
                                            "iconXemAnh" to InlineTextContent(
                                                Placeholder(
                                                    width = 20.sp,
                                                    height = 20.sp,
                                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Image,
                                                    contentDescription = "Xem ảnh",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier
                                                        .clickable {
                                                            //viewModel.loadAnhTheoChiTiet(item.chiTietYeuCauId)
                                                            // chuyển tab nếu có tab đối chiếu ảnh
                                                        }
                                                )
                                            }
                                        )

                                        Text(
                                            text = annotatedText,
                                            inlineContent = inlineContent,
                                            //maxLines = 4,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )


                                    }

                                    VerticalDivider()
                                    Text(
                                        item.viTri,
                                        Modifier
                                            .weight(1f)
                                            .align(Alignment.CenterVertically)
                                    )
                                    VerticalDivider()
                                    Text(
                                        item.noiDung,
                                        Modifier
                                            .weight(1.2f)
                                            .align(Alignment.CenterVertically)
                                    )
                                }

                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            }
                        }



                        Spacer(Modifier.height(16.dp))
                        Text("KẾT LUẬN: Thiết bị đã hoạt động bình thường sau bảo trì.")

                        if (kyBenA != null || kyBenB != null) {
                            Spacer(Modifier.height(16.dp))
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
                        Surface(
                            tonalElevation = 4.dp,
                            shadowElevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .width(320.dp)
                                .height(540.dp)
                        ) {
                            SignaturePad(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp), // đệm nhẹ để nét không dính viền
                                onSigned = { kyBenA = it }
                            )
                        }

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
                        Surface(
                            tonalElevation = 4.dp,
                            shadowElevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .width(320.dp)
                                .height(540.dp)
                        ) {
                            SignaturePad(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp), // đệm nhẹ để nét không dính viền
                                onSigned = { kyBenB = it }
                            )
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if(selectedTabIndex == 0){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(innerPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        if (kyBenA != null && kyBenB != null) {
                            exportBienBanToPdf(context, thongTin, kyBenA!!, kyBenB!!)
                        } else {
                            Toast.makeText(
                                context,
                                "Vui lòng ký đầy đủ cả hai bên",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    icon = { Icon(Icons.Default.Done, contentDescription = null) },
                    text = { Text("Tạo bản hoàn chỉnh") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(0.9f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                )
            }
        }



    }
}

//fun exportBienBanToPdf(
//    context: Context,
//    thongTin: PhanCongThongTin,
//    kyA: Bitmap,
//    kyB: Bitmap
//) {
//    val pdf = PdfDocument()
//    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//    val page = pdf.startPage(pageInfo)
//    val canvas = page.canvas
//
//    val paint = Paint().apply {
//        color = android.graphics.Color.BLACK
//        isAntiAlias = true
//    }
//
//    var y = 50f
//    val centerX = canvas.width / 2f
//    val marginX = 40f
//    val maxTextWidth = canvas.width - 2 * marginX
//
//    fun drawCenter(text: String, textSize: Float = 14f, bold: Boolean = false) {
//        paint.textSize = textSize
//        paint.isFakeBoldText = bold
//        val x = (canvas.width - paint.measureText(text)) / 2f
//        canvas.drawText(text, x, y, paint)
//        y += 26f
//    }
//
//    fun drawWrappedText(text: String, startX: Float, maxWidth: Float, lineSpacing: Float) {
//        var remainingText = text
//        while (remainingText.isNotEmpty()) {
//            val count = paint.breakText(remainingText, true, maxWidth, null)
//            val line = remainingText.substring(0, count)
//            canvas.drawText(line, startX, y, paint)
//            remainingText = remainingText.substring(count)
//            y += lineSpacing
//        }
//    }
//
//    fun drawLeft(text: String, textSize: Float = 12f, bold: Boolean = false) {
//        paint.textSize = textSize
//        paint.isFakeBoldText = bold
//        drawWrappedText(text, marginX, maxTextWidth, lineSpacing = 22f)
//    }
//
//
//    // ===== Header =====
//    drawCenter("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", textSize = 14f, bold = true)
//    drawCenter("Độc lập – Tự do – Hạnh phúc", textSize = 14f, bold = true)
//    y += 12f
//    drawCenter("BIÊN BẢN NGHIỆM THU", textSize = 16f, bold = true)
//    drawCenter("v/v: bảo trì, sửa chữa thiết bị", textSize = 14f)
//    y += 12f
//
//    // ===== Nội dung =====
//    drawLeft("Căn cứ Giấy đề xuất ngày ... tháng ... năm ... của PHÒNG HÀNH CHÍNH về việc sửa chữa máy móc thiết bị.")
//    y += 8f
//    drawLeft("Hôm nay, ngày ... tháng ... năm ..., chúng tôi gồm có:")
//    drawLeft("Ông/Bà: TRẦN THỊ ADMIN    Chức vụ: Quản trị viên – Phòng Hành chính")
//    drawLeft("Ông/Bà: NGUYỄN VĂN KỸ     Chức vụ: Kỹ thuật viên – Phòng Kỹ thuật")
//    y += 8f
//    drawLeft("Chúng tôi thống nhất nghiệm thu các thiết bị đã sửa chữa như sau:")
//    drawLeft("1. Máy in HP 1020 – P.203: Thay hộp mực, vệ sinh")
//    drawLeft("2. Máy chiếu Epson X500 – P.205: Kiểm tra nguồn và ống kính")
//    y += 8f
//    drawLeft("Chúng tôi xác nhận các thiết bị trên đã hoạt động tốt sau khi sửa chữa, bảo trì.")
//    y += 8f
//    drawLeft("Biên bản gồm 01 trang, lập thành 02 bản, mỗi bên giữ một bản và có giá trị pháp lý như nhau.")
//    y += 40f
//
//    // ===== Chữ ký =====
//    paint.textSize = 12f
//    paint.isFakeBoldText = true
//    canvas.drawText("ĐD Đơn vị sử dụng thiết bị", marginX, y, paint)
//    canvas.drawText("ĐD Phòng Kỹ thuật", marginX + 320f, y, paint)
//
//    paint.isFakeBoldText = false
//    canvas.drawText("(Ký, ghi rõ họ tên)", marginX + 5f, y + 18f, paint)
//    canvas.drawText("(Ký, ghi rõ họ tên)", marginX + 325f, y + 18f, paint)
//
//    // ===== Ảnh chữ ký =====
//    val sigSize = 100
//    canvas.drawBitmap(kyA.scale(sigSize, sigSize, false), marginX + 10f, y + 40f, null)
//    canvas.drawBitmap(kyB.scale(sigSize, sigSize, false), marginX + 330f, y + 40f, null)
//
//    pdf.finishPage(page)
//
//    val file = File(
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//        "bien_ban_nghiem_thu_mau_chuan_fix.pdf"
//    )
//    pdf.writeTo(FileOutputStream(file))
//    pdf.close()
//
//    Toast.makeText(
//        context,
//        "✅ Đã tạo PDF mẫu đẹp tại:\n${file.absolutePath}",
//        Toast.LENGTH_LONG
//    ).show()
//}
fun exportBienBanToPdf(
    context: Context,
    thongTin: ThongTinNghiemThu,
    kyA: Bitmap,
    kyB: Bitmap
) {
    val pdf = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    var page = pdf.startPage(pageInfo)
    var canvas = page.canvas

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

    var pageCount = 1

    fun newPageIfNeeded(extraHeight: Float = 30f) {
        if (y + extraHeight > canvas.height - 40f) {
            pdf.finishPage(page)
            page = pdf.startPage(pageInfo)
            canvas = page.canvas
            y = 50f
            pageCount++
        }
    }

    // ===== Header =====
    drawCenter("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", textSize = 14f, bold = true)
    drawCenter("Độc lập – Tự do – Hạnh phúc", textSize = 14f, bold = true)
    y += 12f
    drawCenter("BIÊN BẢN NGHIỆM THU", textSize = 16f, bold = true)
    drawCenter("v/v: bảo trì, sửa chữa thiết bị", textSize = 14f)
    y += 12f

    // ===== Nội dung =====
    drawLeft("Căn cứ theo yêu cầu đã được phê duyệt và phân công xử lý.")
    y += 8f
    val currentDate = SimpleDateFormat("dd 'tháng' MM 'năm' yyyy", Locale("vi")).format(Date())
    drawLeft("Hôm nay, ngày $currentDate, chúng tôi gồm có:")
    drawLeft("BÊN A: ${thongTin.tenBenA} – Đơn vị: ${thongTin.donViBenA}")
    drawLeft("BÊN B: ${thongTin.tenBenB} – Đơn vị: Phòng Kỹ thuật")
    y += 8f
    drawLeft("Chúng tôi thống nhất nghiệm thu các thiết bị đã xử lý như sau:")

    // ===== Danh sách thiết bị (bảng) =====
    y += 10f
    paint.textSize = 12f
    paint.isFakeBoldText = true
    canvas.drawText("STT", marginX, y, paint)
    canvas.drawText("Tên thiết bị", marginX + 40f, y, paint)
    canvas.drawText("Vị trí", marginX + 200f, y, paint)
    canvas.drawText("Nội dung xử lý", marginX + 320f, y, paint)
    y += 18f

    paint.isFakeBoldText = false
    val maxTenThietBiWidth = 140f
    val maxViTriWidth = 100f
    val maxNoiDungWidth = 220f
    val lineHeight = 18f

    thongTin.danhSachThietBi.forEachIndexed { index, item ->

        val tenLines = mutableListOf<String>()
        var remainingTen = item.tenThietBi
        while (remainingTen.isNotEmpty()) {
            val count = paint.breakText(remainingTen, true, maxTenThietBiWidth, null)
            tenLines += remainingTen.substring(0, count)
            remainingTen = remainingTen.substring(count)
        }

        val viTriLines = mutableListOf<String>()
        var remainingViTri = item.viTri
        while (remainingViTri.isNotEmpty()) {
            val count = paint.breakText(remainingViTri, true, maxViTriWidth, null)
            viTriLines += remainingViTri.substring(0, count)
            remainingViTri = remainingViTri.substring(count)
        }

        val noiDungLines = mutableListOf<String>()
        var remainingNoiDung = item.noiDung
        while (remainingNoiDung.isNotEmpty()) {
            val count = paint.breakText(remainingNoiDung, true, maxNoiDungWidth, null)
            noiDungLines += remainingNoiDung.substring(0, count)
            remainingNoiDung = remainingNoiDung.substring(count)
        }

        val maxLineCount = listOf(tenLines.size, viTriLines.size, noiDungLines.size).maxOrNull() ?: 1
        val requiredHeight = maxLineCount * lineHeight + 8f

        newPageIfNeeded(requiredHeight) // ✅ Trước khi vẽ thiết bị này

        for (i in 0 until maxLineCount) {
            newPageIfNeeded(lineHeight) // ✅ Check từng dòng nếu cần

            if (i == 0) {
                canvas.drawText("${index + 1}", marginX, y, paint)
            }

            tenLines.getOrNull(i)?.let {
                canvas.drawText(it, marginX + 40f, y, paint)
            }

            viTriLines.getOrNull(i)?.let {
                canvas.drawText(it, marginX + 200f, y, paint)
            }

            noiDungLines.getOrNull(i)?.let {
                canvas.drawText(it, marginX + 320f, y, paint)
            }

            y += lineHeight
        }

        y += 8f
    }



    y += 8f
    drawLeft("Tất cả các thiết bị trên đã hoạt động bình thường sau bảo trì, sửa chữa.")
    y += 8f
    drawLeft("Biên bản gồm $pageCount trang, lập thành 02 bản, mỗi bên giữ một bản và có giá trị pháp lý như nhau.")
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

    val donViName = normalizeFileName(thongTin.donViBenA)
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
    val fileName = "bien_ban_${donViName}_$timestamp.pdf"


    val file = File(

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        fileName
    )
    pdf.writeTo(FileOutputStream(file))
    pdf.close()

    Toast.makeText(
        context,
        "✅ Đã tạo PDF tại:\n${file.absolutePath}",
        Toast.LENGTH_LONG
    ).show()


}


@Composable
fun VerticalDivider(
    color: Color = MaterialTheme.colorScheme.outline,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color)
    )
}


fun normalizeFileName(name: String): String {
    val temp = Normalizer.normalize(name, Normalizer.Form.NFD)
    return temp.replace(Regex("[^\\p{ASCII}]"), "") // Bỏ dấu
        .replace(" ", "_")                          // Đổi khoảng trắng thành _
        .lowercase(Locale.getDefault())             // Chuyển thành chữ thường
}



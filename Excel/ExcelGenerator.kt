package com.example.utilities

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.R
import com.example.api.model.payment_statement.PaymentData
import com.example.api.model.payment_statement.PaymentDetailsResponse
import org.apache.poi.hssf.usermodel.HSSFFont
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.IndexedColors
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ExcelGenerator(private val context: Context) {

    private var filePath: String = ""

    fun writeExcel(model: PaymentDetailsResponse): String {

        val workBook = HSSFWorkbook()
        val sheet = workBook.createSheet("TransactionNo_${model.transactionNo}")
        val font1 = workBook.createFont().apply {
            fontHeightInPoints = 12
            boldweight = HSSFFont.BOLDWEIGHT_BOLD
            color = IndexedColors.BLACK.getIndex()
            fontName = HSSFFont.FONT_ARIAL
        }
        val font2 = workBook.createFont().apply {
            fontHeightInPoints = 12
            color = IndexedColors.BLACK.getIndex()
            fontName = HSSFFont.FONT_ARIAL
        }
        val cs = workBook.createCellStyle().apply {
            alignment = CellStyle.ALIGN_CENTER
            setFont(font1)
        }
        val cs1 = workBook.createCellStyle().apply {
            alignment = CellStyle.ALIGN_CENTER
            setFont(font2)
        }

        val row0 = sheet.createRow(0)
        with(row0) {
            createCell(0).apply {
                setCellValue("TransactionNo")
                setCellStyle(cs)
            }
            createCell(1).apply {
                setCellValue("ModeOfPayment")
                setCellStyle(cs)
            }
            createCell(2).apply {
                setCellValue("TotalOrderCount")
                setCellStyle(cs)
            }
            createCell(3).apply {
                setCellValue("NetPaidAmount")
                setCellStyle(cs)
            }
        }

        val row1 = sheet.createRow(1)
        with(row1) {
            createCell(0).apply {
                setCellValue(model.transactionNo)
                setCellStyle(cs1)
            }
            createCell(1).apply {
                setCellValue(model.modeOfPayment)
                setCellStyle(cs1)
            }
            createCell(2).apply {
                setCellValue(model.orderList.size.toString())
                setCellStyle(cs1)
            }
            createCell(3).apply {
                setCellValue(model.netPaidAmount.toString())
                setCellStyle(cs1)
            }
        }

        val row3 = sheet.createRow(3)
        with(row3) {
            createCell(0).apply {
                setCellValue("BookingCode")
                setCellStyle(cs)
            }
            createCell(1).apply {
                setCellValue("Quantity")
                setCellStyle(cs)
            }
            createCell(2).apply {
                setCellValue("SalesPrice")
                setCellStyle(cs)
            }
            createCell(3).apply {
                setCellValue("Commission")
                setCellStyle(cs)
            }
            createCell(4).apply {
                setCellValue("PaidAmount")
                setCellStyle(cs)
            }
        }

        model.orderList.forEachIndexed { index, data ->

            val row = sheet.createRow(4 + index)
            with(row) {
                createCell(0).apply {
                    setCellValue(data.bookingCode.toString())
                    setCellStyle(cs1)
                }
                createCell(1).apply {
                    setCellValue(data.quantity.toString())
                    setCellStyle(cs1)
                }
                createCell(2).apply {
                    setCellValue(data.salesPrice.toString())
                    setCellStyle(cs1)
                }
                createCell(3).apply {
                    setCellValue(data.commission.toString())
                    setCellStyle(cs1)
                }
                createCell(4).apply {
                    setCellValue(data.paidAmount.toString())
                    setCellStyle(cs1)
                }
            }
        }

        var filePath = ""
        val fileName = "${model.transactionNo}"
        val outputStream = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), context.getString(R.string.app_name))
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)
            filePath = file.absolutePath
            FileOutputStream(file)
        } else {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + context.getString(R.string.app_name))
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                filePath = FileUtils(context).getPath(uri)
                resolver.openOutputStream(uri)
            }
        }
        outputStream?.use { stream ->
            workBook.write(stream)
        }
        Timber.d("writeExcel $filePath")
        //context.toast("আপনি সফলভাবে এক্সেল শীটে সেভ করেছেন\n$filePath", Toast.LENGTH_LONG)

        return filePath
    }

}
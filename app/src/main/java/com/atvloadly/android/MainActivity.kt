package com.atvloadly.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : Activity() {
    private var selectedIpa: Uri? = null
    private var selectedP12: Uri? = null
    private var selectedProfile: Uri? = null
    private var target = Target.APPLE_TV
    private lateinit var fileLabel: TextView
    private lateinit var targetLabel: TextView
    private lateinit var addressInput: EditText
    private lateinit var pinInput: EditText
    private lateinit var certificateLabel: TextView
    private lateinit var profileLabel: TextView
    private lateinit var certificatePassword: EditText
    private lateinit var status: TextView

    private enum class Target { APPLE_TV, IPHONE_IPAD }

    private val ipaPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            selectedIpa = uri
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val name = uri.lastPathSegment?.substringAfterLast('/') ?: "IPA đã chọn"
            fileLabel.text = "Đã chọn: $name"
            status.text = "Đã nhận tệp IPA. Chưa ký hoặc cài đặt."
        }
    }

    private val p12Picker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            selectedP12 = uri
            certificateLabel.text = "Đã chọn chứng chỉ P12"
            status.text = "Đã chọn P12. Chưa nhập hoặc gửi chứng chỉ đi đâu."
        }
    }

    private val profilePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            selectedProfile = uri
            profileLabel.text = "Đã chọn provisioning profile"
            status.text = "Đã chọn profile. Chưa kiểm tra thiết bị/loại profile."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(10, 15, 28)
        window.navigationBarColor = Color.rgb(10, 15, 28)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(24), dp(22), dp(24))
            setBackgroundColor(Color.rgb(10, 15, 28))
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(content)

        content.addView(text("ATVLoadly", 32, Color.WHITE, true))
        content.addView(text("IPA Manager · Apple TV · iPhone · iPad", 15, 0xFFB6C4DA.toInt()))
        content.addView(space(22))

        content.addView(text("1. Chọn thiết bị đích", 18, Color.WHITE, true))
        content.addView(space(8))
        val tvButton = button("Apple TV") { setTarget(Target.APPLE_TV) }
        val iosButton = button("iPhone / iPad") { setTarget(Target.IPHONE_IPAD) }
        content.addView(tvButton)
        content.addView(space(6))
        content.addView(iosButton)
        targetLabel = text("Đích: Apple TV", 14, 0xFF8CC8FF.toInt())
        content.addView(targetLabel)
        content.addView(space(18))

        content.addView(text("2. Chọn tệp IPA", 18, Color.WHITE, true))
        content.addView(space(8))
        content.addView(button("Chọn IPA từ thiết bị Android") {
            ipaPicker.launch(arrayOf("application/octet-stream", "application/zip", "*/*"))
        })
        fileLabel = text("Chưa chọn tệp .ipa", 14, 0xFFD9E2F1.toInt())
        content.addView(fileLabel)
        content.addView(space(18))

        content.addView(text("3. Kết nối / ký và cài", 18, Color.WHITE, true))
        content.addView(space(8))
        addressInput = edit("Địa chỉ IP / máy chủ (nếu cần)")
        content.addView(addressInput)
        content.addView(space(8))
        pinInput = edit("Mã ghép đôi Apple TV (nếu cần)")
        content.addView(pinInput)
        content.addView(space(8))
        content.addView(text("Chứng chỉ (tùy chọn)", 16, Color.WHITE, true))
        content.addView(space(6))
        content.addView(button("Chọn chứng chỉ .P12") {
            p12Picker.launch(arrayOf("application/x-pkcs12", "application/octet-stream", "*/*"))
        })
        certificateLabel = text("Chưa chọn P12", 14, 0xFFD9E2F1.toInt())
        content.addView(certificateLabel)
        content.addView(space(6))
        content.addView(button("Chọn provisioning profile") {
            profilePicker.launch(arrayOf("application/octet-stream", "*/*"))
        })
        profileLabel = text("Chưa chọn profile", 14, 0xFFD9E2F1.toInt())
        content.addView(profileLabel)
        content.addView(space(6))
        certificatePassword = edit("Mật khẩu P12 (nếu có)")
        certificatePassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        content.addView(certificatePassword)
        content.addView(space(8))
        content.addView(button("Tiếp tục") { validateAndExplain() })
        content.addView(space(16))

        status = text(
            "Bản thử giao diện. Chưa có mô-đun ký IPA, ghép đôi hoặc cài đặt.",
            14,
            0xFFFFD18A.toInt()
        )
        content.addView(status)
        content.addView(space(18))
        content.addView(text(
            "Lưu ý: iPhone/iPad yêu cầu IPA đã ký hợp lệ và phương thức cài tương thích. " +
                "Apple TV dùng quy trình riêng. Chưa có thao tác nào được gửi đến thiết bị.",
            13,
            0xFF9EABC1.toInt()
        ))

        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))
        setContentView(root)
    }

    private fun setTarget(newTarget: Target) {
        target = newTarget
        targetLabel.text = if (target == Target.APPLE_TV) "Đích: Apple TV" else "Đích: iPhone / iPad"
        status.text = if (target == Target.APPLE_TV) {
            "Apple TV: cần triển khai pairing và backend cài đặt."
        } else {
            "iPhone/iPad: cần signing và installer tương thích iOS/iPadOS."
        }
    }

    private fun validateAndExplain() {
        if (selectedIpa == null) {
            status.text = "Hãy chọn tệp IPA trước."
            return
        }
        status.text = if (selectedP12 != null && selectedProfile != null) {
            if (target == Target.APPLE_TV) {
                "Đã chọn IPA, P12 và profile. Chưa thể cài: cần backend ký/cài tvOS."
            } else {
                "Đã chọn IPA, P12 và profile. Chưa thể cài: cần backend ký/cài iOS/iPadOS."
            }
        } else if (target == Target.APPLE_TV) {
            "Đã chọn IPA. Chưa thể cài: backend ghép đôi và cài Apple TV chưa được tích hợp."
        } else {
            "Đã chọn IPA. Chưa thể cài: cần ký IPA và tích hợp phương thức cài iPhone/iPad."
        }
    }

    private fun text(value: String, size: Int, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, dp(5), 0, dp(5))
        }

    private fun button(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        isAllCaps = false
        setOnClickListener { action() }
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun edit(hintText: String) = EditText(this).apply {
        hint = hintText
        singleLine = true
        setTextColor(Color.WHITE)
        setHintTextColor(0xFF8C9BB3.toInt())
        setPadding(dp(12), dp(10), dp(12), dp(10))
        backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF6685B4.toInt())
    }

    private fun space(height: Int) = TextView(this).apply {
        height = dp(height)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}

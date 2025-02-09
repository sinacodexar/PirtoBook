package ir.sina.pirtobook.proxy

import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.InetAddress

// تعریف کلاس ShecanDns برای استفاده از DNS شکن
class ShecanDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://dns.google/resolve?name=$hostname&type=A")
            .build()

        val response = client.newCall(request).execute()
        val json = JSONObject(response.body?.string() ?: "{}")
        val answer = json.optJSONArray("Answer") ?: return Dns.SYSTEM.lookup(hostname)

        return (0 until answer.length()).mapNotNull {
            val record = answer.optJSONObject(it)
            record?.optString("data")?.let { InetAddress.getByName(it) }
        }
    }
}

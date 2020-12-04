package ru.lemaitre.listcontacts.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.lemaitre.listcontacts.BuildConfig
import ru.lemaitre.listcontacts.R
import java.io.File

class FileRepository(
    private val context: Context
) {
    private val sharePrefs by lazy {
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private var state: String = context.getString(R.string.nothing)
    fun getState() = state

    suspend fun downloadFile(link: String) {
        //проверка есть ли такой же файл
//        val savedFile = sharePrefs.all.keys.contains(link)
//        Log.d("TAG", "saved=$savedFile link=$link")
//        if (savedFile) {
//            state = context.getString(R.string.file_exists)
//            return
//        }
        //имя файла timestamp_name.type
//        val nameFile = "textfile.txt"
        Log.d("TAG", "$nameFile")
        //скачиваем файл
        withContext(Dispatchers.IO) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@withContext
            //указываем куда будем сохранять данные
            val testFolder =
                context.getExternalFilesDir("saveFolder")
            val testFile = File(testFolder, nameFile)
            //сохраняем данные
            try {
                testFile.outputStream()
                    .use { fileOutputStream ->
                        Networking.api
                            .getFile(link)
                            .byteStream()
                            .use { inputStream ->
                                inputStream.copyTo(fileOutputStream)
                            }
                    }
                state = context.getString(R.string.success_download, nameFile)
            } catch (t: Throwable) {
                //если ошибка файл удаляем
                testFile.delete()
                Log.d("TAG", "ошибка при загрузке, файл удален ", t)
            }
        }
    }

    suspend fun shareFile(): Uri {
        return withContext(Dispatchers.IO) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                Uri.parse("")
            } else {
                val testFolder = context.getExternalFilesDir("saveFolder")
                val file = File(testFolder, nameFile)
                if (file.exists().not()) {
                    Uri.parse("")
                }
                FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.file_provider",
                    file
                )
            }
        }

    }

    suspend fun saveInSharedPreference(link: String) {
        withContext(Dispatchers.IO) {
            val textToSave = toPatternNameFile(link)
            sharePrefs.edit()
                .putString(link, textToSave)
                .apply()
        }
    }

    private fun toPatternNameFile(link: String): String {
        val index = link.lastIndexOf('/')
        val timeStamp = System.currentTimeMillis().toString()
        return timeStamp + "_" + link.substring(index + 1)
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "saved_prefers"
        private const val nameFile = "textfile.txt"

    }
}


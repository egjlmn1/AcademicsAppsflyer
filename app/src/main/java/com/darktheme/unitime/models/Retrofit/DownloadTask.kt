package com.darktheme.unitime.models.Retrofit

import android.R
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadTask(private val context: Context, downloadUrl: String, filename: String) {
    private var downloadUrl = ""
    private var downloadFileName = ""
    private var progressDialog: ProgressDialog? = null

    private inner class DownloadingTask :
        AsyncTask<Void?, Void?, Void?>() {
        var apkStorage: File? = null
        var outputFile: File? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Downloading...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        override fun onPostExecute(result: Void?) {
            try {
                if (outputFile != null) {
                    progressDialog!!.dismiss()
                    val ctw =
                        ContextThemeWrapper(context, R.style.TextAppearance_Theme)
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(ctw)
                    alertDialogBuilder.setTitle("Document  ")
                    alertDialogBuilder.setMessage("Document Downloaded Successfully ")
                    alertDialogBuilder.setCancelable(false)
                    alertDialogBuilder.setPositiveButton(
                        "ok",
                        DialogInterface.OnClickListener { dialog, id -> })
                    alertDialogBuilder.setNegativeButton(
                        "Open report",
                        DialogInterface.OnClickListener { dialog, id ->
                            val pdfFile = File(
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/Academics/" + downloadFileName
                            ) // -> filename = maven.pdf
                            val path: Uri = Uri.fromFile(pdfFile)
                            val pdfIntent = Intent(Intent.ACTION_VIEW)
                            pdfIntent.setDataAndType(path, "application/pdf")
                            pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            try {
                                context.startActivity(pdfIntent)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "No Application available to view PDF",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    alertDialogBuilder.show()
                    //                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Handler().postDelayed({ }, 3000)
                    Log.e(TAG, "Download Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                //Change button text if exception occurs
                Handler().postDelayed({ }, 3000)
                Log.e(
                    TAG,
                    "Download Failed with Exception - " + e.localizedMessage
                )
            }
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                println("downloading from " + downloadUrl)
                println("filename: " + downloadFileName)
                val url = URL(downloadUrl) //Create Download URl
                val c =
                    url.openConnection() as HttpURLConnection //Open Url Connection
                c.requestMethod = "GET" //Set Request Method to "GET" since we are grtting data
                c.connect() //connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(
                        TAG, "Server returned HTTP " + c.responseCode
                                + " " + c.responseMessage
                    )
                }


                //Get File if SD card is present
                if (CheckForSDCard().isSDCardPresent) {
                    apkStorage = File(
                        Environment.getExternalStorageDirectory()
                            .toString() + "/" + "CodePlayon"
                    )
                } else Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT)
                    .show()

                //If File is not present create directory
                if (!apkStorage!!.exists()) {
                    apkStorage!!.mkdir()
                    Log.e(TAG, "Directory Created.")
                }
                outputFile =
                    File(apkStorage, downloadFileName) //Create Output file in Main File

                //Create New File if not present
                if (!outputFile!!.exists()) {
                    outputFile!!.createNewFile()
                    Log.e(TAG, "File Created")
                }
                val fos =
                    FileOutputStream(outputFile) //Get OutputStream for NewFile Location
                val `is` = c.inputStream //Get InputStream for connection
                val buffer = ByteArray(1024) //Set buffer type
                var len1 = 0 //init length
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }

                //Close all connection after doing task
                fos.close()
                `is`.close()
            } catch (e: Exception) {

                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e(
                    TAG,
                    "Download Error Exception " + e.message
                )
            }
            return null
        }
    }

    companion object {
        private const val TAG = "Download Task"
    }

    init {
        this.downloadUrl = downloadUrl
        downloadFileName = filename
        Log.e(TAG, downloadFileName)
        DownloadingTask().execute()
    }
}
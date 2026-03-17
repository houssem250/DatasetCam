package com.example.datasetcam.pipeline.operations

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.example.datasetcam.pipeline.ImageOperation

/**
 * Applies a Gaussian blur to the bitmap using RenderScript.
 * Note: RenderScript is deprecated in Android 12+, but it's the fastest built-in way 
 * for older/current devices until migrating to RenderEffect or Vulkan.
 */
class BlurOperation(private val context: Context, private val radius: Float) : ImageOperation {
    override val name = "Blur (r=$radius)"

    override fun apply(bitmap: Bitmap, metadata: Map<String, Any>?): Bitmap {
        val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        // Overview: use RenderScript or StackBlur algorithm for Gaussian blur

        // RenderScript requires ARGB_8888
        val inputBitmap = if (bitmap.config == Bitmap.Config.ARGB_8888) {
            bitmap
        } else {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        }

        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        
        // Radius must be between 0 and 25
        val safeRadius = radius.coerceIn(0f, 25f)
        intrinsicBlur.setRadius(safeRadius)
        intrinsicBlur.setInput(tmpIn)
        intrinsicBlur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        
        rs.destroy()
        
        return outputBitmap
    }
}

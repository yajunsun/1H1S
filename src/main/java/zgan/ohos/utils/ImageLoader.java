package zgan.ohos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import zgan.ohos.Contracts.IImageloader;
import zgan.ohos.MyApplication;

/**
 * Created by yajunsun on 2015/11/12.
 */
public final class ImageLoader {
    private static final String TAG = "ImageDownloader";
    private static int maxMemery = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static LruCache<String, Bitmap> imageCaches = new LruCache<String, Bitmap>(
            maxMemery / 8) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

//    private int viewWidth = 100;
//    private int viewHeigth = 100;
//    private Bitmap currBitmap = null;

    public ImageLoader() {
    }

    /**
     * 加载drawable资源图片
     *
     * @param context
     * @param resId      资源ID
     * @param mImageView imageview控件
     * @param download   回调接口
     * @param reqWidth   imageview控件的宽
     * @param reqHeight  imageview控件的高
     */
    public void loadDrawableRS(Context context, int resId, View mImageView, IImageloader download, int reqWidth, int reqHeight) {
        if (reqHeight == -1)
            reqHeight = mImageView.getHeight();
        if (reqWidth == -1)
            reqWidth = mImageView.getWidth();
        Bitmap currBitmap = imageCaches.get(String.valueOf(resId));
        if (currBitmap == null) {
            currBitmap = decodeSampledBitmapFromResource(context.getResources(), resId, reqWidth, reqHeight);
        }
        if (currBitmap != null) {
            Add2Cache(String.valueOf(resId), currBitmap);
            //mImageView.setImageBitmap(currBitmap);
            if (download != null)
                download.onDownloadSucc(currBitmap, String.valueOf(resId), mImageView);
        }
    }

    /**
     * 加载drawable资源图片
     *
     * @param context
     * @param resId      资源ID
     * @param mImageView imageview控件
     * @param download   回调接口
     */
    public void loadDrawableRs(Context context, int resId, View mImageView, IImageloader download) {
        loadDrawableRS(context, resId, mImageView, download, -1, -1);
    }


    /**
     * 加载SD卡图片或网络图片
     *
     * @param imageName  图片uri
     * @param mImageView imageview控件
     * @param download   回调接口
     */
    public void loadImage(String imageName, View mImageView,
                          IImageloader download) {
        loadImage(imageName, mImageView, download,
                -1, -1);
    }

    /**
     * 加载SD卡图片或网络图片
     *
     * @param uri   图片uri
     * @param mImageView  显示图片的控件
     * @param download    回调接口
     * @param _viewWidth  图像控件的宽
     * @param _viewHeigth 图像控件的高
     */
    public void loadImage(final String uri, final View mImageView,
                          final IImageloader download,
                          int _viewWidth, int _viewHeigth) {
        if (_viewHeigth == -1)
            _viewHeigth = mImageView.getHeight();
        if (_viewWidth == -1)
            _viewWidth = mImageView.getWidth();
        final String imageName = uri + "(" + _viewWidth + "*" + _viewHeigth + ")";
        final int viewWidth = _viewWidth, viewHeigth = _viewHeigth;
        Bitmap currBitmap = imageCaches.get(imageName);
        if (currBitmap == null) {
            getBitmapFromFile(imageName, _viewWidth, _viewHeigth);
        }
        if (currBitmap != null && mImageView != null) {
            download.onDownloadSucc(currBitmap, imageName, mImageView);
        } else if (currBitmap == null) {

            ImageRequest imageRequest = new ImageRequest(uri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    Log.v("suntest", "request:" + uri);
                    try {
                        Bitmap dstbmp = null;
                        if (response.getWidth() > viewWidth && response.getHeight() > viewHeigth)
                            dstbmp = Bitmap.createBitmap(response, 0, 0, viewWidth, viewHeigth);
                        else
                            dstbmp = response;
                        if (imageName != null) {
                            Add2Cache(imageName, dstbmp);
                        }


                        download.onDownloadSucc(dstbmp, imageName, mImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    viewWidth, viewHeigth, ImageView.ScaleType.CENTER, Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("suntest", error.getMessage());
                        }
                    });
            MyApplication.requestQueue.add(imageRequest);
        }
    }

    /**
     * 加载Assets中的图片
     *
     * @param context
     * @param imgname   图片名
     * @param imageView imageview控件
     * @param download  回调接口
     */
    public void loadAssetsRS(Context context, String imgname, ImageView imageView, IImageloader download) {
        loadAssetsRS(context, imgname, imageView, download);
    }

    /**
     * 加载Assets中的图片
     *
     * @param context
     * @param imgname    图片名
     * @param mImageView imageview控件
     * @param download   回调接口
     * @param reqWidth   图像控件的宽
     * @param reqHeight  图像控件的高
     */
    public void loadAssetsRS(Context context, String imgname, ImageView mImageView, IImageloader download, int reqWidth, int reqHeight) {
        if (reqHeight == -1)
            reqHeight = mImageView.getHeight();
        if (reqWidth == -1)
            reqWidth = mImageView.getWidth();
        final int viewHeigth = reqHeight,
                viewWidth = reqWidth;
        Bitmap currBitmap = imageCaches.get(imgname);
        if (currBitmap == null) {
            currBitmap = decodeSampledBitmapFromAssets(context.getResources(), imgname, reqWidth, reqHeight);
        }
        if (currBitmap != null) {
            Add2Cache(imgname, currBitmap);
            //mImageView.setImageBitmap(currBitmap);
            if (download != null) download.onDownloadSucc(currBitmap, imgname, mImageView);
        }
    }

    /************
     * local method
     ************/
    public static void Add2Cache(String key, Bitmap bitmap) {
        if (Get4Cache(key) == null)
            imageCaches.put(key, bitmap);
    }

    public static Bitmap Get4Cache(String key) {
        return imageCaches.get(key);
    }

    public static void Remove4Cache(String key) {
        if (Get4Cache(key) != null)
            imageCaches.remove(key);
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                   int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public Bitmap decodeSampledBitmapFromAssets(Resources res, String imgname,
                                                 int reqWidth, int reqHeight) {
        try {
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream is = res.getAssets().open(imgname);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            int inSampleSize = 1;
            inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            is.close();
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception ex) {
            return null;
        }
    }


    public void getBitmapFromFile(String imageName, int viewWidth, int viewHeigth) {
        if (imageName != null) {
            File file;
            String real_path;
            try {
                real_path = picturefile.getappdirpic();
                if (imageName.startsWith(real_path))
                    file = new File(imageName);
                else
                    file = new File(real_path, imageName);
                if (file.exists()) {
                    FileInputStream fs = new FileInputStream(file);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(fs, null, options);
                    int inSampleSize;
                    inSampleSize = calculateInSampleSize(options, viewWidth, viewHeigth);
                    options.inSampleSize = inSampleSize;
                    options.inJustDecodeBounds = false;
                    fs.close();
                    fs = new FileInputStream(file);
                    Bitmap currBitmap = BitmapFactory.decodeStream(fs, null, options);
                    Add2Cache(imageName, currBitmap);
                    fs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean setBitmapToFile(String path, Activity mActivity,
                                    String imageName, Bitmap bitmap) {
        File file;
        String real_path;
        try {
            if (generalhelper.hasSDCard()) {
                real_path = generalhelper.getExtPath()
                        + (path != null && path.startsWith("/") ? path : "/"
                        + path);
            } else {
                real_path = generalhelper.getPackagePath(mActivity)
                        + (path != null && path.startsWith("/") ? path : "/"
                        + path);
            }
            file = new File(real_path, imageName);
            if (!file.exists()) {
                File file2 = new File(real_path + "/");
                file2.mkdirs();
            }
            file.createNewFile();
            FileOutputStream fos;
            if (generalhelper.hasSDCard()) {
                fos = new FileOutputStream(file);
            } else {
                fos = mActivity.openFileOutput(imageName, Context.MODE_PRIVATE);
            }

            if (imageName != null
                    && (imageName.contains(".png") || imageName
                    .contains(".PNG"))) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            fos.flush();
            if (fos != null) {
                fos.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeBitmapFromFile(String path, Activity mActivity,
                                      String imageName) {
        File file;
        String real_path;
        try {
            if (generalhelper.hasSDCard()) {
                real_path = generalhelper.getExtPath()
                        + (path != null && path.startsWith("/") ? path : "/"
                        + path);
            } else {
                real_path = generalhelper.getPackagePath(mActivity)
                        + (path != null && path.startsWith("/") ? path : "/"
                        + path);
            }
            file = new File(real_path, imageName);
            if (file != null)
                file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /****************************************************/
}

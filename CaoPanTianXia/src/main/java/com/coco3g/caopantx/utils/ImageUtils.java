package com.coco3g.caopantx.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
	public final static String SDCARD_MNT = "/mnt/sdcard";
	public final static String SDCARD = "/sdcard";
	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 请求相机 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

	/**
	 * 图片转换二进制流
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(String path) {
		Bitmap bm = BitmapFactory.decodeFile(path);
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		} else {
			return null;
		}
	}

	/**
	 * 图片转换二进制流
	 *
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		} else {
			return null;
		}
	}

	/**
	 * 获取图片路径 2014年8月12日
	 *
	 * @param uri
	 * @param cursor
	 * @return E-mail:mr.huangwenwei@gmail.com
	 */
	public static String getImagePath(Uri uri, Activity context) {

		String[] projection = { MediaColumns.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			String ImagePath = cursor.getString(columIndex);
			cursor.close();
			return ImagePath;
		}

		return uri.toString();
	}

	static Bitmap bitmap = null;

	/**
	 * 读取相册图片
	 *
	 * @param uri
	 * @param context
	 * @return
	 */
	public static Bitmap loadPicasaImageFromGalley(final Uri uri, final Activity context) {

		String[] projection = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
			if (columIndex != -1) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
			cursor.close();
			return bitmap;
		} else
			return null;
	}

	/**
	 * 获取图片缩略图
	 *
	 * @param context
	 * @param imgName
	 * @param kind
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap loadImgThumbnail(Activity context, String imgName, int kind) {
		Bitmap bitmap = null;

		String[] proj = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME };

		Cursor cursor = context.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName + "'",
				null, null);

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			ContentResolver crThumb = context.getContentResolver();
			Options options = new Options();
			options.inSampleSize = 1;
			bitmap = getThumbnail(crThumb, cursor.getInt(0), kind, options);
		}
		return bitmap;
	}

	public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
		Bitmap bitmap = getBitmapByPath(filePath);
		return zoomBitmap(bitmap, w, h);
	}

	@TargetApi(7)
	public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options) {
		return MediaStore.Images.Thumbnails.getThumbnail(cr, origId, kind, options);
	}

	/**
	 * 获取bitmap
	 *
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	public static Bitmap getBitmapByPath(String filePath, Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 根据图片的url路径获得Bitmap对象
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap returnBitmap(String img_url) {
		try {
			URL url = new URL(img_url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception e) {
			// Log exception
			e.printStackTrace();
			return null;
		}
		// DisplayImageOptions options = new DisplayImageOptionsUtils().init();
		// ImageSize targetSize = new ImageSize(60, 60);
		// Bitmap bitmap = ImageLoader.getInstance().loadImageSync(img_url,
		// targetSize, options);
		// return bitmap;
	}

	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
	 *
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + SDCARD + File.separator;
		String pre2 = "file://" + SDCARD_MNT + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	/**
	 * 通过uri获取文件的绝对路径
	 *
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 下载图片
	 *
	 * @param context
	 * @param url
	 *            下载地址
	 * @param dirpath
	 *            下载目录
	 * @param filename
	 *            文件名F
	 * @return
	 */
	public static String downloadBitmap(Context context, String url, String dirpath, String filename) {
		if (url == null || url.equalsIgnoreCase(""))
			return null;
		//
		File dir = new File(dirpath);
		if (!dir.exists())
			dir.mkdirs();
		File f = new File(dir.getAbsoluteFile() + File.separator + ".nomedia");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String path = dir.getAbsolutePath() + File.separator + filename;
		f = new File(path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] b = null;
		try {
			b = getBlob(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fileoutputstream = null;
		if (b != null) {
			try {
				fileoutputstream = new FileOutputStream(path);
				fileoutputstream.write(b);
				fileoutputstream.flush();
				return path;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (fileoutputstream != null)
						fileoutputstream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			return null;
		}
	}

	public static byte[] getBlob(String url) {
		if (url == null)
			return null;
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		byte[] buf = null;
		HttpGet httpget = null;
		try {
			httpget = new HttpGet(url);
			HttpResponse mResponse;
			HttpEntity mEntity;
			try {
				mResponse = httpclient.execute(httpget);
				int statusCode = mResponse.getStatusLine().getStatusCode();
				if (statusCode != 200)
					return null;
			} catch (ClientProtocolException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
			mEntity = mResponse.getEntity();
			if (mEntity == null)
				return null;
			try {
				buf = EntityUtils.toByteArray(mEntity);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return buf;
	}

	/**
	 * 放大缩小图片
	 *
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		}
		return newbmp;
	}

	/**
	 * 创建缩略图
	 *
	 * @param context
	 * @param largeImagePath
	 *            原始大图路径
	 * @param thumbfilePath
	 *            输出缩略图路径
	 * @param square_size
	 *            输出图片宽度
	 * @param quality
	 *            输出图片质量
	 * @throws IOException
	 */
	public static void createImageThumbnail(Context context, String largeImagePath, String thumbfilePath, int square_size, int quality) throws IOException {
		Options opts = new Options();
		opts.inSampleSize = 1;
		// 原始图片bitmap
		Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

		if (cur_bitmap == null)
			return;

		// 原始图片的高宽
		int[] cur_img_size = new int[] { cur_bitmap.getWidth(), cur_bitmap.getHeight() };
		// 计算原始图片缩放后的宽高
		int[] new_img_size = scaleImageSize(cur_img_size, square_size);
		// 生成缩放后的bitmap
		Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0], new_img_size[1]);
		// 生成缩放后的图片文件
		saveImageToSD(null, thumbfilePath, thb_bitmap, CompressFormat.JPEG, quality);
	}

	/**
	 * 计算缩放图片的宽高
	 * 
	 * @param img_size
	 * @param square_size
	 * @return
	 */
	public static int[] scaleImageSize(int[] img_size, int square_size) {
		if (img_size[0] <= square_size && img_size[1] <= square_size)
			return img_size;
		double ratio = square_size / (double) Math.max(img_size[0], img_size[1]);
		return new int[] { (int) (img_size[0] * ratio), (int) (img_size[1] * ratio) };
	}

	/**
	 * 获取本地图片旋转角度
	 * 
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (bitmap != resizedBitmap) {
			bitmap.recycle();
			bitmap = null;
			System.gc();
		}
		return resizedBitmap;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static String rotaingImageView(Context context, int angle, String path) {
		if (!TextUtils.isEmpty(path)) {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			if (bitmap != null) {
				// 旋转图片 动作
				Matrix matrix = new Matrix();
				matrix.postRotate(angle);
				// 创建新的图片
				Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				try {
					saveImageToSD(context, path, resizedBitmap, CompressFormat.JPEG, 100);
					if (bitmap != resizedBitmap) {
						bitmap.recycle();
						bitmap = null;
						System.gc();
					}
					if (resizedBitmap != null && !resizedBitmap.isRecycled()) {
						resizedBitmap.recycle();
						resizedBitmap = null;
						System.gc();
					}
					return path;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return path;
				}

			} else {
				return path;
			}
		} else {
			return path;
		}

	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(Context ctx, String filePath, Bitmap bitmap, CompressFormat format, int quality) throws IOException {
		if (bitmap != null) {
			File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
			bitmap.compress(format, quality, bos);
			bos.flush();
			bos.close();
			if (ctx != null) {
				scanPhoto(ctx, filePath);
			}
		}
	}

	/**
	 * 让Gallery上能马上看到该图片
	 */
	private static void scanPhoto(Context ctx, String imgFileName) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(imgFileName);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		ctx.sendBroadcast(mediaScanIntent);
	}
}

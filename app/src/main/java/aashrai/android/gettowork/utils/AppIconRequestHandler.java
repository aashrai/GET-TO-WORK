package aashrai.android.gettowork.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

public class AppIconRequestHandler extends RequestHandler {

    /** Uri scheme for app icons */
    private static final String SCHEME_APP_ICON = "app-icon";

    private PackageManager mPackageManager;

    public AppIconRequestHandler(Context context) {
        mPackageManager = context.getPackageManager();
    }

    /**
     * Create an Uri that can be handled by this RequestHandler based on the package name.
     */
    public static Uri getUri(String packageName) {
        return Uri.fromParts(SCHEME_APP_ICON, packageName, null);
    }

    @Override
    public boolean canHandleRequest(Request data) {
        return (SCHEME_APP_ICON.equals(data.uri.getScheme()));
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        String packageName = request.uri.getSchemeSpecificPart();
        Drawable drawable;
        try {
            drawable = mPackageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException ignored) {
            return null;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return new Result(bitmap, Picasso.LoadedFrom.DISK);
    }
}
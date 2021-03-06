package es.iessaladillo.pedrojoya.pr188.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class IntentsUtils {

    private IntentsUtils() {
    }

    public static void startInstalledAppDetailsActivity(@NonNull final Context context) {
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    public static Intent newViewFileIntent(Context context, File file, String
        mimeType) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(
            FileProvider.getUriForFile(context, "es.iessaladillo.pedrojoya.pr188.provider", file)
            , mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

}

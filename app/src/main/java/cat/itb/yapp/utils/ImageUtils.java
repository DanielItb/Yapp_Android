package cat.itb.yapp.utils;

import android.content.Context;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageUtils {
    private final Context context;

    public ImageUtils(Context context) {
        this.context = context;
    }

    public void cutImage(Uri imageUri, Fragment fragment) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(300, 300)
                .setAspectRatio(1, 1)
                .start(context, fragment);
    }
}

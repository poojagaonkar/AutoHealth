package autohealth.pgapps.com.autohealth.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by HomePC on 19-03-16.
 */
public class DialogHelper {

    public void CreateErrorDialog(Context appContext,String title, String message)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(appContext);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setCancelable(false);
        alert.create().show();
    }
}

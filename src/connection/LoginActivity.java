package connection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.jk.hfh_app.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {

    private EditText loginView;
    private EditText passwordView;

    private String login;
    private String base64passwordHash;

    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.login);

        this.loginView = (EditText) findViewById(R.id.et_login);
        this.passwordView = (EditText) findViewById(R.id.et_password);
    }


    public void logIn(View view) throws NoSuchAlgorithmException {
        String password = this.passwordView.getText().toString();
        byte[] hashPassword = hashPassword(password);
        this.login = this.loginView.getText().toString();
        this.base64passwordHash = Base64.encodeToString(hashPassword, Base64.DEFAULT);
    }


    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        String sha1 = "";
        byte[] passwordHash = new byte[128];
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        digest.reset();
        digest.update(password.getBytes());
        passwordHash = digest.digest();

        return passwordHash;
    }
}

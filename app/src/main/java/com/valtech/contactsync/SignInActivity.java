package com.valtech.contactsync;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;

public class SignInActivity extends AccountAuthenticatorActivity {
  private static final int SYNC_INTERVAL = 4 * 3600 * 1000;

  private ApiClient apiClient = new ApiClient();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    WebView webView = new WebView(this);
    WebViewClient client = new IdpWebViewClient();
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(client);
    setContentView(webView);

    webView.loadUrl(apiClient.getAuthorizeUrl(), new HashMap<String, String>() {{
      put("X-Idp-Client-Type", "native");
    }});
  }

  private class IdpWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      return url.startsWith("vidp://");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      if (!url.startsWith("vidp://")) {
        super.onPageStarted(view, url, favicon);
        return;
      }

      Uri uri = Uri.parse(url);
      final String code = uri.getQueryParameter("code");

      new SignInTask().execute(code);
    }
  }

  private class SignInTask extends AsyncTask<String, Void, Bundle> {
    @Override
    protected Bundle doInBackground(String... params) {
      ApiClient.TokenResponse tokenResponse = apiClient.getAccessTokenAndRefreshToken(params[0]);
      ApiClient.UserInfoResponse userInfoResponse = apiClient.getUserInfoMeResource(tokenResponse.accessToken);

      AccountManager accountManager = AccountManager.get(SignInActivity.this);
      Account account = new Account(userInfoResponse.email, getString(R.string.account_type));
      accountManager.addAccountExplicitly(account, tokenResponse.refreshToken, null);

//      ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
//      ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);

      //ContentResolver.addPeriodicSync(account, getString(R.string.account_authority), null, SYNC_INTERVAL);
//
//      Bundle settings = new Bundle();
//      settings.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//      settings.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//      ContentResolver.requestSync(account, getString(R.string.account_authority), settings);

      Bundle result = new Bundle();
      result.putString(AccountManager.KEY_ACCOUNT_NAME, userInfoResponse.email);
      result.putString(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.account_type));
      return result;
    }

    @Override
    protected void onPostExecute(Bundle result) {
      SignInActivity.this.setAccountAuthenticatorResult(result);
      SignInActivity.this.finish();
    }
  }
}
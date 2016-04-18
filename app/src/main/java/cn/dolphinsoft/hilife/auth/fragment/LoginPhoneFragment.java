package cn.dolphinsoft.hilife.auth.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

import cn.dolphinsoft.hilife.R;
import cn.dolphinsoft.hilife.common.util.DialogUtil;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginPhoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginPhoneFragment extends Fragment implements View.OnClickListener{
    /* UI组件 */
    private Button mButton;
    private EditText mEditText;
    private TextView mLoginTipTextView;
    private TextView mLoginHelpTextView;
    private ImageButton mImageButton;

    private Fragment loginCodeFragment = new LoginCodeFragment();

    private OnFragmentInteractionListener mListener;

    public LoginPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* 初始化UI */
        View view = inflater.inflate(R.layout.fragment_login_phone,container,false);
        mButton = (Button)view.findViewById(R.id.phone_next_step_button);
        mEditText = (EditText)view.findViewById(R.id.phone);
        mLoginTipTextView = (TextView)view.findViewById(R.id.login_tip);
        mLoginHelpTextView = (TextView)view.findViewById(R.id.login_help_tip);
        mImageButton = (ImageButton)getActivity().findViewById(R.id.img_btn_arrow_left);

        mLoginHelpTextView.setOnClickListener(this);
        mImageButton.setOnClickListener(this);
        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_btn_arrow_left:
                getActivity().finish();
                break;
            case R.id.phone_next_step_button:
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mEditText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                checkUser();
                break;
            case R.id.login_help_tip:
                break;
        }
    }

    /* 检测用户是否存在 */
    private void checkUser(){
        // 清空错误提示控件内容
        mLoginTipTextView.setText("");

        String phone = mEditText.getText().toString().trim();

        if(!isPhoneValid(phone)){
            mLoginTipTextView.setVisibility(View.VISIBLE);
            mLoginTipTextView.setText(getString(R.string.error_invalid_phone));
        }
        else {
            String url = (getString(R.string.contextPath) + getString(R.string.checkUser)).trim();
            RequestParams params = new RequestParams();
            params.put("loginId", phone);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url,params,new JsonHttpResponseHandler(){
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (statusCode == 200) {
                        try {
                            if (response.getString("code").equals("NACK")) {
                                DialogUtil dialogUtil = new DialogUtil(getActivity());
                                dialogUtil.setMessage("您的手机号尚未注册，点击注册即表示您同意我们的服务条款，系统将自动为您创建账号！");
                                dialogUtil.setNegativeButton("取消", null);
                                dialogUtil.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        attemptGetSmsCode();
                                    }
                                });
                                dialogUtil.show();
                            } else if(response.getString("code").equals("ACK")){
                                attemptGetSmsCode();
                            }else{
                                Toast.makeText(getActivity(), response.getString("code"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_SHORT).show();
                }
             });
        }
    }

    /* 获取验证码相关操作 */
    private void attemptGetSmsCode(){
        String phone = mEditText.getText().toString().trim();

        String url = (getString(R.string.contextPath) + getString(R.string.sendLoginSms)).trim();
        StringEntity se = null;
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loginId",phone);
            se = new StringEntity(jsonObject.toString());
        }catch(JSONException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getActivity(),url, se,"application/json", new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                switchFragment();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void switchFragment(){
        FragmentTransaction transaction = getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
        Bundle args = new Bundle();
        args.putString("loginId", mEditText.getText().toString().trim());
        loginCodeFragment.setArguments(args);
        transaction.replace(R.id.activity_login, loginCodeFragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean isPhoneValid(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.length()==11){
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

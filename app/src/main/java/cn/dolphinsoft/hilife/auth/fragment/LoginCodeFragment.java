package cn.dolphinsoft.hilife.auth.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cn.dolphinsoft.hilife.R;
import cn.dolphinsoft.hilife.auth.domain.LoginEntity;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginCodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginCodeFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* UI组件 */
    private Button mButton;
    private EditText mEditText;
    private TextView mLoginTipTextView;
    private TextView mLoginCodeHelpTextView;
    private ImageButton mImageButton;

    private OnFragmentInteractionListener mListener;

    public LoginCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginCodeFragment newInstance(String param1, String param2) {
        LoginCodeFragment fragment = new LoginCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_code, container, false);
        mButton = (Button)view.findViewById(R.id.login_button);
        mEditText = (EditText)view.findViewById(R.id.password);
        mLoginTipTextView = (TextView)view.findViewById(R.id.login_tip);
        mLoginCodeHelpTextView = (TextView)view.findViewById(R.id.login_code_help_tip);
        mImageButton = (ImageButton)getActivity().findViewById(R.id.img_btn_arrow_left);

        mLoginCodeHelpTextView.setOnClickListener(this);
        mImageButton.setOnClickListener(this);
        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_btn_arrow_left:
                getFragmentManager().popBackStack();
                break;
            case R.id.login_button:
                attemptLogin();
                break;
            case R.id.login_code_help_tip:
                break;
        }
    }

    /* 登录相关操作 */
    private void attemptLogin() {
        // 清空错误提示控件内容
        mLoginTipTextView.setText(null);

        String password = mEditText.getText().toString();

        // 检查输入的验证码是否有效
        if (!isPasswordValid(password)) {
            mLoginTipTextView.setVisibility(View.VISIBLE);
            mLoginTipTextView.setText(getString(R.string.error_invalid_password));
        } else {
            String url = (getString(R.string.contextPath) + getString(R.string.login_authc)).trim();
            StringEntity se = null;
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("loginId",getArguments().getString("loginId"));
                jsonObject.put("password",password);
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
                    if(statusCode == 200){
                        try{
                            if(response.getString("code").equals("ACK")){
                                JSONObject data = response.getJSONObject("data");
                                writeUserToSDCard(data.getString("loginId"),data.getString("token"));
                                Intent result = new Intent();
                                result.putExtra("loginStatus",response.getString("message").trim());
                                getActivity().setResult(Activity.RESULT_OK,result);
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), response.getString("code"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
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

    private void writeUserToSDCard(String loginId,String token){
        FinalDb db = FinalDb.create(getActivity(),"cn.dolphinsoft.hilife");

        LoginEntity entity = new LoginEntity();
        entity.setLoginId(loginId);
        entity.setToken(token);

        db.save(entity);
    }

    private boolean isPasswordValid(String password) {
        if(!TextUtils.isEmpty(password) && password.length() == 6){
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

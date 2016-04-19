package cn.dolphinsoft.hilife.main.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dolphinsoft.hilife.R;
import cn.dolphinsoft.hilife.main.view.NoScrollGridView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment{

    private OnFragmentInteractionListener mListener;

    /* GridView组件 */
    private NoScrollGridView mCleanGridView,mMaintainGridView,mElectricGridView,mHomelyGridView,mMoveGridView,mPipelineGridView,mOtherGridView;

    /* GridView适配器 */
    private SimpleAdapter cleanAdapter,electricAdapter,homelyAdapter,maintainAdapter,moveAdapter,otherAdapter,pipelineAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        initGridView(view);
        return view;
    }

    /* 初始化GridView 和它的Adapter、listener */
    public void initGridView(View view){
        mCleanGridView = (NoScrollGridView)view.findViewById(R.id.gridview_clean);
        cleanAdapter = new SimpleAdapter(getActivity(),getCleanGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mCleanGridView.setAdapter(cleanAdapter);
        mCleanGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mMaintainGridView = (NoScrollGridView)view.findViewById(R.id.gridview_maintain);
        maintainAdapter = new SimpleAdapter(getActivity(),getMaintainGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mMaintainGridView.setAdapter(maintainAdapter);
        mMaintainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

         mElectricGridView = (NoScrollGridView)view.findViewById(R.id.gridview_electric);
         electricAdapter= new SimpleAdapter(getActivity(),getElectricGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mElectricGridView.setAdapter(electricAdapter);
        mElectricGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mHomelyGridView = (NoScrollGridView)view.findViewById(R.id.gridview_homely);
        homelyAdapter = new SimpleAdapter(getActivity(),getHomelyGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mHomelyGridView.setAdapter(homelyAdapter);
        mHomelyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mMoveGridView = (NoScrollGridView)view.findViewById(R.id.gridview_move);
        moveAdapter = new SimpleAdapter(getActivity(),getMoveGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mMoveGridView.setAdapter(moveAdapter);
        mMoveGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mPipelineGridView = (NoScrollGridView)view.findViewById(R.id.gridview_pipeline);
        pipelineAdapter= new SimpleAdapter(getActivity(),getPipelineGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mPipelineGridView.setAdapter(pipelineAdapter);
        mPipelineGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mOtherGridView = (NoScrollGridView)view.findViewById(R.id.gridview_other);
        otherAdapter = new SimpleAdapter(getActivity(),getOtherGridViewData(),
                R.layout.gridview_content,new String[]{"itemIcon","itemText"},
                new int[]{R.id.item_icon,R.id.item_text});
        mOtherGridView.setAdapter(otherAdapter);
        mOtherGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private List<Map<String,Object>> getCleanGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_clean_deep,R.mipmap.ic_clean_daily,R.mipmap.ic_clean_window,
                R.mipmap.ic_clean_kitchen,R.mipmap.ic_clean_new_house,R.mipmap.ic_clean_shower};
        String[] listText = new String[]{getString(R.string.deep),getString(R.string.daily),getString(R.string.window),getString(R.string.kitchen),getString(R.string.newhouse),getString(R.string.shower)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
    }

    private List<Map<String,Object>> getMaintainGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_maintain_wood,R.mipmap.ic_maintain_mason,R.mipmap.ic_maintain_plumber,
                R.mipmap.ic_maintain_electric,R.mipmap.ic_maintain_painter};
        String[] listText = new String[]{getString(R.string.wood),getString(R.string.mason),getString(R.string.plumber),
                getString(R.string.m_electric),getString(R.string.painter)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
    }

    private List<Map<String,Object>> getElectricGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_electric_gas,R.mipmap.ic_electric_microwave,R.mipmap.ic_electric_refrigerator,
                R.mipmap.ic_electric_rangehood,R.mipmap.ic_electric_air_condition,R.mipmap.ic_electric_washin,
                R.mipmap.ic_electric_disinfection,R.mipmap.ic_electric_oven,R.mipmap.ic_electric_water};
        String[] listText = new String[]{getString(R.string.gas),getString(R.string.microwave),getString(R.string.refrigerator),
                getString(R.string.rangehood),getString(R.string.aircondition),getString(R.string.washin),
                getString(R.string.distinfection),getString(R.string.oven),getString(R.string.water)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
    }

    private List<Map<String,Object>> getHomelyGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_homely_sofa,R.mipmap.ic_homely_floor};
        String[] listText = new String[]{getString(R.string.sofa),getString(R.string.floor)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
    }

    private List<Map<String,Object>> getMoveGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_move_house,R.mipmap.ic_move_factory,R.mipmap.ic_move_weight};
        String[] listText = new String[]{getString(R.string.house),getString(R.string.factory),getString(R.string.weight)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
    }

    private List<Map<String,Object>> getPipelineGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_pipeline_sewer,R.mipmap.ic_pipeline_closetool,R.mipmap.ic_pipeline_bathtub};
        String[] listText = new String[]{getString(R.string.sewer),getString(R.string.closetool),getString(R.string.bathtub)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
    }

    private List<Map<String,Object>> getOtherGridViewData(){
        List<Map<String,Object>> items = new ArrayList<>();
        int[] listIcon = new int[]{R.mipmap.ic_other_dry_clean,R.mipmap.ic_other_aunt,R.mipmap.ic_other_ch2o};
        String[] listText = new String[]{getString(R.string.dryclean),getString(R.string.aunt),getString(R.string.ch2o)};
        for(int i = 0;i < listIcon.length; i++){
            Map<String,Object> item = new HashMap<>();
            item.put("itemIcon",listIcon[i]);
            item.put("itemText",listText[i]);
            items.add(item);
        }
        return items;
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
        void onFragmentInteraction(Uri uri);
    }
}

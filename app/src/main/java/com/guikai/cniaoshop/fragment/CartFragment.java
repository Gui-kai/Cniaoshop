package com.guikai.cniaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.guikai.cniaoshop.CniaoApplication;
import com.guikai.cniaoshop.CreateOrderActivity;
import com.guikai.cniaoshop.LoginActivity;
import com.guikai.cniaoshop.R;
import com.guikai.cniaoshop.adapter.CartAdapter;
import com.guikai.cniaoshop.adapter.decoration.DividerItemDecortion;
import com.guikai.cniaoshop.bean.ShoppingCart;
import com.guikai.cniaoshop.bean.User;
import com.guikai.cniaoshop.http.OkHttpHelper;
import com.guikai.cniaoshop.utils.CartProvider;
import com.guikai.cniaoshop.widget.CnToolbar;

import java.util.List;

public class CartFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CartFragment";

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;

    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;
    private Button mBtnOrder;
    private Button mBtnDel;
    private CnToolbar mToolbar;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;
    
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartProvider = new CartProvider(getContext());
        initView(view);
        showData();

        return view;
    }


    public void refData(){
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }



    private void showData(){

        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter = new CartAdapter(getContext(),carts,mCheckBox,mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecortion.VERTICAL_LIST));
    }

    private void initView(View view) {
        mToolbar = (CnToolbar) view.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mTextTotal = (TextView) view.findViewById(R.id.txt_total);
        mBtnOrder = (Button) view.findViewById(R.id.btn_order);
        mBtnDel = (Button) view.findViewById(R.id.btn_del);

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.delCart();
            }
        });

        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toOrder();
            }
        });

        changeToolbar();
    }

    public void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);


    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();
        if(ACTION_EDIT == action){

            showDelControl();
        }
        else if(ACTION_CAMPLATE == action){

            hideDelControl();
        }
    }


    private void toOrder() {
        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);

        startActivity(intent,true);
        getActivity().finish();
//        httpHelper.get(Contants.API.USER_DETAIL, new SpotsCallBack<User>(getActivity()) {
//            @Override
//            public void onSuccess(Call call, Response response, User o) {
//                Log.e(TAG, "onSuccess: "+response.code());
//            }
//
//            @Override
//            public void onError(Call call, Response response, int code, Exception e) {
//                Log.e(TAG, "onError: "+response.code());
//            }
//        });
    }

    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    private void hideDelControl(){

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    public void startActivity(Intent intent, boolean isNeedLogin){

        if (isNeedLogin) {
            User user = CniaoApplication.getmInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            }
            else {
                CniaoApplication.getmInstance().putIntent(intent);
                Intent LoginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(LoginIntent);
            }
        } else {
            super.startActivity(intent);
        }

    }
}



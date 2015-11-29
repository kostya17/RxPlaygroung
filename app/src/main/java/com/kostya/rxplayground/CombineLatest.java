package com.kostya.rxplayground;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.internal.util.SubscriptionList;

public class CombineLatest extends Fragment {

    @Bind(R.id.name)
    protected EditText name;
    @Bind(R.id.password)
    protected EditText password;
    @Bind(R.id.address)
    protected EditText address;
    @Bind(R.id.submit)
    protected Button submit;

    private Subscription subscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combine_latest, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Observable<CharSequence> nameObservable = RxTextView.textChanges(name);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(password);
        Observable<CharSequence> addressObservable = RxTextView.textChanges(address);

        subscription = Observable.combineLatest(nameObservable, passwordObservable, addressObservable, (name1, password1, address1) -> {
            boolean isNameValid = !TextUtils.isEmpty(name1) && name1.length() > 1;
            boolean isPasswordValid = !TextUtils.isEmpty(password1) && password1.length() >= 6;
            boolean isAddressValid = !TextUtils.isEmpty(address1) && address1.length() >= 5;

            return isNameValid && isPasswordValid && isAddressValid;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(submit::setEnabled);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null) subscription.unsubscribe();
    }
}

package org.frozenbox.frozenchat.ui.adapter;

import java.util.List;

import org.frozenbox.frozenchat.R;
import org.frozenbox.frozenchat.entities.Account;
import org.frozenbox.frozenchat.ui.XmppActivity;
import org.frozenbox.frozenchat.ui.ManageAccountActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Switch;

public class AccountAdapter extends ArrayAdapter<Account> {

	private XmppActivity activity;

	public AccountAdapter(XmppActivity activity, List<Account> objects) {
		super(activity, 0, objects);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Account account = getItem(position);
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.account_row, parent, false);
		}
		TextView jid = (TextView) view.findViewById(R.id.account_jid);
		jid.setText(account.getJid().toBareJid().toString());
		TextView statusView = (TextView) view.findViewById(R.id.account_status);
		ImageView imageView = (ImageView) view.findViewById(R.id.account_image);
		imageView.setImageBitmap(activity.avatarService().get(account, activity.getPixel(48)));
		statusView.setText(getContext().getString(account.getStatus().getReadableId()));
		switch (account.getStatus()) {
			case ONLINE:
				statusView.setTextColor(activity.getOnlineColor());
				break;
			case DISABLED:
			case CONNECTING:
				statusView.setTextColor(activity.getSecondaryTextColor());
				break;
			default:
				statusView.setTextColor(activity.getWarningTextColor());
				break;
		}
		final Switch tglAccountState = (Switch) view.findViewById(R.id.tgl_account_status);
		boolean isDisabled = (account.getStatus() == Account.State.DISABLED) ? true : false;
		tglAccountState.setChecked(!isDisabled);
		tglAccountState.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (activity instanceof ManageAccountActivity) {
					((ManageAccountActivity) activity).onClickTglAccountState(account);
					tglAccountState.toggle();
				}
			}
		});
		return view;
	}
}

package me.bakumon.moneykeeper.ui.setting;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;

/**
 * 恢复备份对话框
 *
 * @author Bakumon https://bakumon.me
 */
public class BackupFliesDialog {
    private Context mContext;
    private List<BackupBean> mBackupBeans;
    private BottomSheetDialog mDialog;
    private OnItemClickListener mListener;

    public BackupFliesDialog(Context context, List<BackupBean> beans) {
        mContext = context;
        mBackupBeans = beans;
        setupDialog();
    }

    private void setupDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View contentView = layoutInflater.inflate(R.layout.dialog_backup_files, null, false);
        RecyclerView rvFiles = contentView.findViewById(R.id.rv_files);
        rvFiles.setLayoutManager(new LinearLayoutManager(mContext));
        FilesAdapter adapter = new FilesAdapter(null);
        rvFiles.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            dismiss();
            if (mListener != null) {
                mListener.onClick(adapter.getData().get(position).file);
            }
        });
        adapter.setNewData(mBackupBeans);

        mDialog = new BottomSheetDialog(mContext);
        mDialog.setContentView(contentView);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(File file);
    }

    class FilesAdapter extends BaseDataBindingAdapter<BackupBean> {

        FilesAdapter(@Nullable List<BackupBean> data) {
            super(R.layout.item_backup_files, data);
        }

        @Override
        protected void convert(DataBindingViewHolder helper, BackupBean item) {
            ViewDataBinding binding = helper.getBinding();

            binding.setVariable(BR.backupBean, item);

            binding.executePendingBindings();
        }
    }

}

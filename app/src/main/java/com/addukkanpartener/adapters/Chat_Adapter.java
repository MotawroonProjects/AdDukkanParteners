package com.addukkanpartener.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ChatImageLeftRowBinding;
import com.addukkanpartener.databinding.ChatImageRightRowBinding;
import com.addukkanpartener.databinding.ChatMsgLeftBinding;
import com.addukkanpartener.databinding.ChatMsgRightBinding;
import com.addukkanpartener.databinding.LoadMoreRowBinding;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;
    private final int ITEM_image_LEFT = 3;
    private final int ITEM_image_RIGHT = 4;
    private final int LOAD = 5;

    private final String lang;


    private int current_user_id;
    private Context context;
    private LayoutInflater inflater;
    //   private ChatActivity activity;

    public Chat_Adapter(Context context) {
        this.current_user_id = current_user_id;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        // activity = (ChatActivity) context;
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_MESSAGE_RIGHT) {
            ChatMsgRightBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_msg_right, parent, false);
            return new RightMessageEventHolder(binding);

        } else if (viewType == ITEM_MESSAGE_LEFT) {
            ChatMsgLeftBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_msg_left, parent, false);
            return new LeftMessageEventHolder(binding);

        } else if (viewType == ITEM_image_LEFT) {
            ChatImageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row, parent, false);
            return new LeftImageEventHolder(binding);

        } else if (viewType == ITEM_image_RIGHT) {
            ChatImageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row, parent, false);
            return new RightImageEventHolder(binding);

        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RightImageEventHolder) {
            RightImageEventHolder eventHolder = (RightImageEventHolder) holder;
//                eventHolder.binding.setMessagemodel(messageModel);
//                eventHolder.binding.setLang(lang);

        } else if (holder instanceof LeftImageEventHolder) {
            LeftImageEventHolder eventHolder = (LeftImageEventHolder) holder;

//                eventHolder.binding.setMessagemodel(messageModel);
//                eventHolder.binding.setLang(lang);


        } else if (holder instanceof RightMessageEventHolder) {
            RightMessageEventHolder eventHolder = (RightMessageEventHolder) holder;
//                if (messageModel.getMessage() != null) {
//
//                    eventHolder.binding.setMessagemodel(messageModel);
//                    eventHolder.binding.setLang(lang);
//                } else {
//                    eventHolder.itemView.setVisibility(View.GONE);
//                }

        } else if (holder instanceof LeftMessageEventHolder) {
            LeftMessageEventHolder eventHolder = (LeftMessageEventHolder) holder;

//                if (messageModel.getMessage() != null) {
//
//                    eventHolder.binding.setMessagemodel(messageModel);
//                    eventHolder.binding.setLang(lang);
//                } else {
//                    eventHolder.itemView.setVisibility(View.GONE);
//                }


        } else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public LoadMoreHolder(LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public class RightMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMsgRightBinding binding;

        public RightMessageEventHolder(@NonNull ChatMsgRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LeftMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMsgLeftBinding binding;

        public LeftMessageEventHolder(@NonNull ChatMsgLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LeftImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageLeftRowBinding binding;

        public LeftImageEventHolder(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class RightImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageRightRowBinding binding;

        public RightImageEventHolder(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
//        MessageModel messageModel = messageModelList.get(position);
//        if (messageModel != null) {
////            if (messageModel.getTo_user_id().equals(current_user_id + "")) {
////                //  Log.e("type",messageModel.getType());
////                if (messageModel.getType().equals("message")) {
////                    return ITEM_MESSAGE_LEFT;
////                } else {
////                    return ITEM_image_LEFT;
////                }
////            } else {
////                if (messageModel.getType().equals("message")) {
////                    return ITEM_MESSAGE_RIGHT;
////                } else {
////                    return ITEM_image_RIGHT;
////                }
////
////            }
//        } else {
//            return LOAD;
//        }
        if (position == 0) {
            return ITEM_MESSAGE_LEFT;
        } else if (position == 1) {
            return ITEM_image_LEFT;
        } else if (position == 2) {
            return ITEM_image_RIGHT;
        } else {
            return ITEM_MESSAGE_RIGHT;
        }
    }
}

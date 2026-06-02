package com.shrazavi.dadmehr.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.DataClass.Like;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Post;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapterTalar extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String myid;
    String content;
    public int like;
//    public SharedPreferences preferences;
    private ArrayList<Post> postItems;
    Retrofitinformation RI;
    private Context context;
    private final int VIEW_TYPE_POST_ITEM = 0;
    private final int VIEW_TYPE_LOADING_ITEM = 1;
    int idauthor;
    private OnLoadMoreListener mOnLoadMoreListener;
    private View.OnClickListener mOnClickListener;
    private boolean islike = false;
    private boolean isLoading;
    private boolean isFail;
    private int lastPosition = -1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;


    public RecyclerAdapterTalar(Context context, ArrayList<Post> postItems, RecyclerView mRecyclerView, String content) {
        this.context = context;
        this.content = content;
        this.postItems = postItems;
        Log.e("posr rec", postItems.size() + "");

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                    isFail = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return postItems == null ? 0 : postItems.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnRetryListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (postItems.get(position) == null)
            return VIEW_TYPE_LOADING_ITEM;
        else
            return VIEW_TYPE_POST_ITEM;
    }

    public void setLoaded(boolean loaded) {
        isLoading = loaded;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_POST_ITEM) {
            View view = G.inflater.inflate(R.layout.item_post, parent, false);
            return new postViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING_ITEM) {
            View view = G.inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
//        vu = BasicActivity.vu;
        myid = BasicActivity.userid;

        if (holder instanceof postViewHolder) {

            final postViewHolder Holder = (postViewHolder) holder;
            Post post = postItems.get(listPosition);
//            like = post.getLike();

            Holder.txtPostname.setTypeface(G.face);
            Holder.txtPosttext.setTypeface(G.face);
            Holder.txtlike.setTypeface(G.face);

            Holder.txtPostname.setText(post.getUsername().toString());
            Picasso.with(G.context).load(G.nodeurl + post.getImg()).into(Holder.imgpost);
//            Log.e("content",content+"");
            Call<Vakil> callvakil = RI.getvakil(content, post.getUsername(), myid);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                    Holder.txtPostname.setText(response.body().getName());
//                    Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");
                    if (!response.body().getProfile().equals("empty")) {
                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(Holder.imgprof);

                    }
                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {

                }
            });
            Call<Like> calllike = RI.getlike(content, post.getId(), myid);
            calllike.enqueue(new Callback<Like>() {
                @Override
                public void onResponse(Call<Like> call, Response<Like> response) {
//                    Log.e("like",response.body()+"");
                    if(response.body()!=null) {
                        if (response.body().getLike()) {
                            islike = true;
                            Holder.btnlike.setBackgroundResource(R.drawable.ic_like_on);
                        } else {
                            islike = false;
                            Holder.btnlike.setBackgroundResource(R.drawable.ic_like_off);
                        }

//                    Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");
                    }else {
                        islike = false;
                        Holder.btnlike.setBackgroundResource(R.drawable.ic_like_off);
                    }
                }

                @Override
                public void onFailure(Call<Like> call, Throwable t) {

                }
            });
            Call<ArrayList<Like>> callpost = RI.getlikepost(post.getId());
            callpost.enqueue(new Callback<ArrayList<Like>>() {
                @Override
                public void onResponse(Call<ArrayList<Like>> call, Response<ArrayList<Like>> response) {
//                    Holder.txtlike.setText(response.body().size() + "نفر پسندیده");
//                    Log.e("liket", response.body().size() + "");
//like=response.body().size();

                  like=response.body().size();
                    Holder.txtlike.setText(like + "نفر پسندیده");
                }

                @Override
                public void onFailure(Call<ArrayList<Like>> call, Throwable t) {
                    Log.e("liket", t + "");
                }
            });

            Holder.btnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("like", islike+"");
                    if (islike) {
                        Log.e(" postid",  post.getId()+"");
                        islike = false;
                        Call<MessageSignup> callvakil = RI.like(content, post.getId(), post.getUsername(), myid, islike, myid);
                        callvakil.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                Log.e("like messsage=",response.body().getMessage()+"");
                                Holder.btnlike.setBackgroundResource(R.drawable.ic_like_off);

                                like--;
                                Holder.txtlike.setText(like + "نفر پسندیده");
                                Log.e("likef", like + "");
                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {

                            }
                        });

//                        Log.e("likef", islike + "");
                    } else {

                        islike = true;
                        Call<MessageSignup> callvakil = RI.like(content, post.getId(), post.getUsername(), myid, islike, myid);
                        callvakil.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                Log.e("like messsage=",response.body().getMessage()+"");
                                Holder.btnlike.setBackgroundResource(R.drawable.ic_like_on);
                                like++;
                                Log.e("likef", like + "");
                                Holder.txtlike.setText(like + "نفر پسندیده");
                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {

                            }
                        });
//                        Log.e("liket",islike+"");
//
                    }
                }
            });

            Holder.txtPosttext.setText(post.getText());
            Holder.txtlike.setText(post.getLike() + "نفر پسندیده");
//            Holder.txtPosttext.addShowMoreText("بیشتر");
//            Holder.txtPosttext.addShowLessText("کمتر");
//            Holder.txtPosttext.setShowMoreColor(Color.RED); // or other color
//            Holder.txtPosttext.setShowLessTextColor(Color.RED); // or other color
//            mPostViewHolder.txtAuthorName.setText(post.getAuthorName());

//            mPostViewHolder.crdHolder.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent(PostsRecyclerAdapter.this.context, DetailPostActivity.class);
//                    intent.putExtra("id", postItems.get(listPosition).getId()+"");
//                    intent.putExtra("title", postItems.get(listPosition).getTitle().toString());
//                    intent.putExtra("content", postItems.get(listPosition).getContent().toString());
//                    context.startActivity(intent);
//                }
//            });

        }
        if (listPosition > lastPosition) {

//            Animation animation = AnimationUtils.loadAnimation(context,
//                    R.anim.layout_animation);
//            holder.itemView.startAnimation(animation);
            lastPosition = listPosition;
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder mLoadingViewHolder = (LoadingViewHolder) holder;
//            mLoadingViewHolder.btnRetry.setTypeface(G.face);
            if (isFail == true) {
                mLoadingViewHolder.btnRetry.setVisibility(View.VISIBLE);
                mLoadingViewHolder.progressBar.setVisibility(View.GONE);
                mLoadingViewHolder.btnRetry.setOnClickListener(this.mOnClickListener);
            } else {
                mLoadingViewHolder.btnRetry.setVisibility(View.GONE);
                mLoadingViewHolder.imgreload.setVisibility(View.GONE);
                mLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
            }
        }

    }

    class postViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPostname;
        //        private HtmlTextView txtPostExcerpt;
        private ExpandableTextView txtPosttext;
        private Button btnlike;
        private TextView txtlike;
        private ImageView imgpost;
        private ImageView imgprof;
        private CardView crdHolder;

        public postViewHolder(View itemView) {
            super(itemView);

            txtPostname = (TextView) itemView.findViewById(R.id.txt_post_name);
            txtPosttext = (ExpandableTextView) itemView.findViewById(R.id.txt_post_text);
//            txtPostDate = (TextView) itemView.findViewById(R.id.itemTxtPostDate);
            txtlike = (TextView) itemView.findViewById(R.id.txt_post_like);
            imgpost = (ImageView) itemView.findViewById(R.id.img_post);
            imgprof = (ImageView) itemView.findViewById(R.id.img_post_profile);
            btnlike = (Button) itemView.findViewById(R.id.btn_post_like);
            crdHolder = (CardView) itemView.findViewById(R.id.crd_post);

//            txtPostTitle.setTypeface(G.faceBold);
//            txtPostDate.setTypeface(G.face);
//            txtPostExcerpt.setTypeface(G.face);
//            txtAuthorName.setTypeface(G.face);
        }

    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        private MaterialProgressBar progressBar;
        private Button btnRetry;
        private ImageView imgreload;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            imgreload = (ImageView) itemView.findViewById(R.id.imgreload);
            progressBar = (MaterialProgressBar) itemView.findViewById(R.id.progressBarL);
            btnRetry = (Button) itemView.findViewById(R.id.btnRetryL);
        }
    }

}
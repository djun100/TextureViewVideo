package com.example.textureviewvideo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity{

	private MediaPlayer mMediaPlayer;
	private Surface surface;
	
	private ImageView videoImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextureView textureView=(TextureView) findViewById(R.id.textureview);
		textureView.setSurfaceTextureListener(surfaceTextureListener);//设置监听函数  重写4个方法

		videoImage=(ImageView) findViewById(R.id.video_image);
	}

	private SurfaceTextureListener surfaceTextureListener=new SurfaceTextureListener() {
		//SurfaceTexture可用
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width,int height) {
			surface=new Surface(surfaceTexture);
			new PlayerVideoThread().start();//开启一个线程去播放视频
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,int height) {//尺寸改变
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {//销毁
			surface=null;
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer=null;
			return true;
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {//更新
		}
	};

	private class PlayerVideoThread extends Thread{
		@Override
		public void run(){
			 try {
				  mMediaPlayer= new MediaPlayer();
				  Uri uri = Uri.parse("android.resource://com.example.textureviewvideo/"+R.raw.ansen);
				  mMediaPlayer.setDataSource(MainActivity.this,uri);//设置播放资源(可以是应用的资源文件／url／sdcard路径)
				  mMediaPlayer.setSurface(surface);//设置渲染画板
				  mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
				  mMediaPlayer.setOnCompletionListener(onCompletionListener);//播放完成监听
				  mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {//预加载监听
					@Override
					public void onPrepared(MediaPlayer mp){//预加载完成
						videoImage.setVisibility(View.GONE);//隐藏图片
						mMediaPlayer.start();//开始播放
					}
				  });
				  mMediaPlayer.prepare();
			  } catch (Exception e) {  
				  e.printStackTrace();
			  }
	    }
	}

	private MediaPlayer.OnCompletionListener onCompletionListener=new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {//播放完成
			videoImage.setVisibility(View.VISIBLE);
		}
	};
}

package zgan.ohos.services.jtws;

import android.os.Message;
import android.util.Log;


import zgan.ohos.utils.Frame;

import java.util.LinkedList;

public class ZganJTWSService_Main implements Runnable {
	private java.util.Queue<byte[]> Queue=new LinkedList<byte[]>();
	private java.util.Queue<Frame> Queue_Function=new LinkedList<Frame>();
	private boolean isGetData=false;
	private int intSendOutTime=300; //30秒
	private boolean isSendOutTime=false;
	private  int intTime=0;
	private Frame getFrame;
	
	public ZganJTWSService_Main(java.util.Queue<byte[]> _queue, java.util.Queue<Frame> _fqueue){
		Queue=_queue;
		Queue_Function=_fqueue;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread tt=new Thread(new Thread_SendOutTime());
		tt.start();
		
		while(true){
			try {
				Thread.sleep(100);

				if(ZganJTWSServiceTools.isConnect && ZganJTWSServiceTools.isLoginOK &&
						Queue_Function!=null && Queue_Function.size()>0){
					getFrame=Queue_Function.poll();
					
					isGetData=true;
					
					//发送数据				
					ZganJTWSServiceTools.toSendMsg(getFrame);
					Log.e("发送数据时间", System.currentTimeMillis() + "");
					intTime=0;
					isSendOutTime=true;
					

					//接收数据				
					while(isGetData){
						if(Queue.size()>0){						
							byte[] resultByte=null;
							resultByte=Queue.poll();
							
							Frame f=new Frame(resultByte);
							

							if(f!=null && getFrame.mainCmd==f.mainCmd 
									&& getFrame.subCmd==f.subCmd
									&& getFrame.version==f.version 
									&& getFrame._handler!=null){						
										
								intTime=0;
								isSendOutTime=false;
								
						        Message msg = getFrame._handler.obtainMessage();
						        msg.obj = f;
						        msg.what  = 1;
								
								getFrame._handler.sendMessage(msg);								
								
								isGetData=false;								
							}
	
						}
					}
					
				}
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
		}
		
	}
	
	//判断数据发送超时
	private class Thread_SendOutTime implements Runnable {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub			
			
			while(true){
			try {
				Thread.sleep(100);
				
				if(isSendOutTime){
						if(intSendOutTime==intTime){
							intTime=0;
							isSendOutTime=false;
							isGetData=false;
							
					        Message msg = getFrame._handler.obtainMessage();
					        msg.what  = 0;
							
							getFrame._handler.sendMessage(msg);		
							
							Log.i("ZganJTWSService_Main", "接收超时");
						}else{
							intTime++;
						}

		
					}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}				
			
		}		
	};

}

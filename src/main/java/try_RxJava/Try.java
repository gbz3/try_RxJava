package try_RxJava;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class Try {

	public static void main(String[] args) throws Exception {
		Flowable<String> flowable = Flowable.create( new FlowableOnSubscribe<String>() {

			public void subscribe( FlowableEmitter<String> emitter ) throws Exception {
				String[] datas = { "Hello, World!", "Ç±ÇÒÇ…ÇøÇÕÅAê¢äE!" };
				
				for( String data: datas ) {
					if( emitter.isCancelled() ) {
						return;
					}
					emitter.onNext( data );
				}
				
				emitter.onComplete();
			}
			
		}, BackpressureStrategy.BUFFER );
		
		flowable.observeOn( Schedulers.computation() ).subscribe( new Subscriber<String>() {
			private Subscription sub;

			public void onSubscribe( Subscription sub ) {
				this.sub = sub;
				this.sub.request( 1L );
			}
			
			public void onNext( String data ) {
				String threadName = Thread.currentThread().getName();
				System.out.println( threadName + " : " + data );
				this.sub.request( 1L );
			}

			public void onComplete() {
				String threadName = Thread.currentThread().getName();
				System.out.println( threadName + " : äÆóπÇµÇ‹ÇµÇΩ" );
			}

			public void onError( Throwable t ) {
				t.printStackTrace();
			}

		});
		
		Thread.sleep( 500L );

	}

}

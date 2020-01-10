package com.iflytek.tps.beans.core;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(value = "by gRPC proto compiler (version 1.4.0)", comments = "Source: dictation.proto")
public final class IatGrpc {

	private IatGrpc() {
	}

	public static final String SERVICE_NAME = "iat.Iat";

	// Static method descriptors that strictly reflect the proto.
	@io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
	public static final io.grpc.MethodDescriptor<Dictation.IatRequest, Dictation.IatResult> METHOD_CREATE_REC = io.grpc.MethodDescriptor.<Dictation.IatRequest, Dictation.IatResult>newBuilder()
			.setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING).setFullMethodName(generateFullMethodName("iat.Iat", "createRec"))
			.setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(Dictation.IatRequest.getDefaultInstance()))
			.setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(Dictation.IatResult.getDefaultInstance())).build();

	/**
	 * Creates a new async stub that supports all call types for the service
	 */
	public static IatStub newStub(io.grpc.Channel channel) {
		return new IatStub(channel);
	}

	/**
	 * Creates a new blocking-style stub that supports unary and streaming output calls on the service
	 */
	public static IatBlockingStub newBlockingStub(io.grpc.Channel channel) {
		return new IatBlockingStub(channel);
	}

	/**
	 * Creates a new ListenableFuture-style stub that supports unary calls on the service
	 */
	public static IatFutureStub newFutureStub(io.grpc.Channel channel) {
		return new IatFutureStub(channel);
	}

	/**
	 */
	public static abstract class IatImplBase implements io.grpc.BindableService {

		/**
		 * <pre>
		 * 采用流的方式持续向服务端写音频数据，及持续从服务端获得结果
		 * 音频请求流,IatRequest.endFlag为true时代表写音频结束
		 * 结果返回流,IatResult.endFlang为true时代表会话识别结束
		 * </pre>
		 */
		public io.grpc.stub.StreamObserver<Dictation.IatRequest> createRec(io.grpc.stub.StreamObserver<Dictation.IatResult> responseObserver) {
			return asyncUnimplementedStreamingCall(METHOD_CREATE_REC, responseObserver);
		}

		@Override
		public final io.grpc.ServerServiceDefinition bindService() {
			return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
					.addMethod(METHOD_CREATE_REC, asyncBidiStreamingCall(new MethodHandlers<Dictation.IatRequest, Dictation.IatResult>(this, METHODID_CREATE_REC))).build();
		}
	}

	/**
	 */
	public static final class IatStub extends io.grpc.stub.AbstractStub<IatStub> {
		private IatStub(io.grpc.Channel channel) {
			super(channel);
		}

		private IatStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
			super(channel, callOptions);
		}

		@Override
		protected IatStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
			return new IatStub(channel, callOptions);
		}

		/**
		 * <pre>
		 * 采用流的方式持续向服务端写音频数据，及持续从服务端获得结果
		 * 音频请求流,IatRequest.endFlag为true时代表写音频结束
		 * 结果返回流,IatResult.endFlang为true时代表会话识别结束
		 * </pre>
		 */
		public io.grpc.stub.StreamObserver<Dictation.IatRequest> createRec(io.grpc.stub.StreamObserver<Dictation.IatResult> responseObserver) {
			return asyncBidiStreamingCall(getChannel().newCall(METHOD_CREATE_REC, getCallOptions()), responseObserver);
		}
	}

	/**
	 */
	public static final class IatBlockingStub extends io.grpc.stub.AbstractStub<IatBlockingStub> {
		private IatBlockingStub(io.grpc.Channel channel) {
			super(channel);
		}

		private IatBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
			super(channel, callOptions);
		}

		@Override
		protected IatBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
			return new IatBlockingStub(channel, callOptions);
		}
	}

	/**
	 */
	public static final class IatFutureStub extends io.grpc.stub.AbstractStub<IatFutureStub> {
		private IatFutureStub(io.grpc.Channel channel) {
			super(channel);
		}

		private IatFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
			super(channel, callOptions);
		}

		@Override
		protected IatFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
			return new IatFutureStub(channel, callOptions);
		}
	}

	private static final int METHODID_CREATE_REC = 0;

	private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>, io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
			io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
		private final IatImplBase serviceImpl;
		private final int methodId;

		MethodHandlers(IatImplBase serviceImpl, int methodId) {
			this.serviceImpl = serviceImpl;
			this.methodId = methodId;
		}

		@Override
		public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
			switch (methodId) {
				default:
					throw new AssertionError();
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
			switch (methodId) {
				case METHODID_CREATE_REC:
					return (io.grpc.stub.StreamObserver<Req>) serviceImpl.createRec((io.grpc.stub.StreamObserver<Dictation.IatResult>) responseObserver);
				default:
					throw new AssertionError();
			}
		}
	}

	private static final class IatDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
		@Override
		public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
			return Dictation.getDescriptor();
		}
	}

	private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

	public static io.grpc.ServiceDescriptor getServiceDescriptor() {
		io.grpc.ServiceDescriptor result = serviceDescriptor;
		if (result == null) {
			synchronized (IatGrpc.class) {
				result = serviceDescriptor;
				if (result == null) {
					serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME).setSchemaDescriptor(new IatDescriptorSupplier()).addMethod(METHOD_CREATE_REC).build();
				}
			}
		}
		return result;
	}
}

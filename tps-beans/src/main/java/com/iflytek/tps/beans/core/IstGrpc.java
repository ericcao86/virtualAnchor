package com.iflytek.tps.beans.core;

import io.grpc.stub.ClientCalls;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.14.0)",
        comments = "Source: Ist.proto")
public final class IstGrpc {

    private IstGrpc() {
    }

    public static final String SERVICE_NAME = "ist.Ist";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<IstOuterClass.IstRequest,
            IstOuterClass.IstResult> getCreateRecMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "createRec",
            requestType = IstOuterClass.IstRequest.class,
            responseType = IstOuterClass.IstResult.class,
            methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
    public static io.grpc.MethodDescriptor<IstOuterClass.IstRequest,
            IstOuterClass.IstResult> getCreateRecMethod() {
        io.grpc.MethodDescriptor<IstOuterClass.IstRequest, IstOuterClass.IstResult> getCreateRecMethod;
        if ((getCreateRecMethod = IstGrpc.getCreateRecMethod) == null) {
            synchronized (IstGrpc.class) {
                if ((getCreateRecMethod = IstGrpc.getCreateRecMethod) == null) {
                    IstGrpc.getCreateRecMethod = getCreateRecMethod =
                            io.grpc.MethodDescriptor.<IstOuterClass.IstRequest, IstOuterClass.IstResult>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
                                    .setFullMethodName(generateFullMethodName(
                                            "ist.Ist", "createRec"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            IstOuterClass.IstRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            IstOuterClass.IstResult.getDefaultInstance()))
                                    .setSchemaDescriptor(new IstMethodDescriptorSupplier("createRec"))
                                    .build();
                }
            }
        }
        return getCreateRecMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static IstStub newStub(io.grpc.Channel channel) {
        return new IstStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static IstBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        return new IstBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static IstFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new IstFutureStub(channel);
    }

    /**
     */
    public static abstract class IstImplBase implements io.grpc.BindableService {

        /**
         * <pre>
         * 采用流的方式持续向服务端写音频数据，及持续从服务端获得结果
         * 音频请求流,IstRequest.endFlag为true时代表写音频结束
         * 结果返回流,IstResult.endFlang为true时代表会话识别结束
         * </pre>
         */
        public io.grpc.stub.StreamObserver<IstOuterClass.IstRequest> createRec(
                io.grpc.stub.StreamObserver<IstOuterClass.IstResult> responseObserver) {
            return asyncUnimplementedStreamingCall(getCreateRecMethod(), responseObserver);
        }

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            getCreateRecMethod(),
                            asyncBidiStreamingCall(
                                    new MethodHandlers<
                                            IstOuterClass.IstRequest,
                                            IstOuterClass.IstResult>(
                                            this, METHODID_CREATE_REC)))
                    .build();
        }
    }

    /**
     */
    public static final class IstStub extends io.grpc.stub.AbstractStub<IstStub> {
        private IstStub(io.grpc.Channel channel) {
            super(channel);
        }

        private IstStub(io.grpc.Channel channel,
                        io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected IstStub build(io.grpc.Channel channel,
                                io.grpc.CallOptions callOptions) {
            return new IstStub(channel, callOptions);
        }

        /**
         * <pre>
         * 采用流的方式持续向服务端写音频数据，及持续从服务端获得结果
         * 音频请求流,IstRequest.endFlag为true时代表写音频结束
         * 结果返回流,IstResult.endFlang为true时代表会话识别结束
         * </pre>
         */
        public io.grpc.stub.StreamObserver<IstOuterClass.IstRequest> createRec(
                io.grpc.stub.StreamObserver<IstOuterClass.IstResult> responseObserver) {
            return ClientCalls.asyncBidiStreamingCall(
                    getChannel().newCall(getCreateRecMethod(), getCallOptions()), responseObserver);
        }
    }

    /**
     */
    public static final class IstBlockingStub extends io.grpc.stub.AbstractStub<IstBlockingStub> {
        private IstBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private IstBlockingStub(io.grpc.Channel channel,
                                io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected IstBlockingStub build(io.grpc.Channel channel,
                                        io.grpc.CallOptions callOptions) {
            return new IstBlockingStub(channel, callOptions);
        }
    }

    /**
     */
    public static final class IstFutureStub extends io.grpc.stub.AbstractStub<IstFutureStub> {
        private IstFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private IstFutureStub(io.grpc.Channel channel,
                              io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected IstFutureStub build(io.grpc.Channel channel,
                                      io.grpc.CallOptions callOptions) {
            return new IstFutureStub(channel, callOptions);
        }
    }

    private static final int METHODID_CREATE_REC = 0;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final IstImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(IstImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_CREATE_REC:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.createRec(
                            (io.grpc.stub.StreamObserver<IstOuterClass.IstResult>) responseObserver);
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class IstBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        IstBaseDescriptorSupplier() {
        }

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return IstOuterClass.getDescriptor();
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("Ist");
        }
    }

    private static final class IstFileDescriptorSupplier
            extends IstBaseDescriptorSupplier {
        IstFileDescriptorSupplier() {
        }
    }

    private static final class IstMethodDescriptorSupplier
            extends IstBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        IstMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (IstGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new IstFileDescriptorSupplier())
                            .addMethod(getCreateRecMethod())
                            .build();
                }
            }
        }
        return result;
    }
}

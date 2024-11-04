package email;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.0)",
    comments = "Source: src/main/java/proto/email.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class EmailServiceGrpc {

  private EmailServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "email.EmailService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<email.EmailOuterClass.EmailRequest,
      email.EmailOuterClass.EmailResponse> getEnviarEmailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EnviarEmail",
      requestType = email.EmailOuterClass.EmailRequest.class,
      responseType = email.EmailOuterClass.EmailResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<email.EmailOuterClass.EmailRequest,
      email.EmailOuterClass.EmailResponse> getEnviarEmailMethod() {
    io.grpc.MethodDescriptor<email.EmailOuterClass.EmailRequest, email.EmailOuterClass.EmailResponse> getEnviarEmailMethod;
    if ((getEnviarEmailMethod = EmailServiceGrpc.getEnviarEmailMethod) == null) {
      synchronized (EmailServiceGrpc.class) {
        if ((getEnviarEmailMethod = EmailServiceGrpc.getEnviarEmailMethod) == null) {
          EmailServiceGrpc.getEnviarEmailMethod = getEnviarEmailMethod =
              io.grpc.MethodDescriptor.<email.EmailOuterClass.EmailRequest, email.EmailOuterClass.EmailResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EnviarEmail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  email.EmailOuterClass.EmailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  email.EmailOuterClass.EmailResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmailServiceMethodDescriptorSupplier("EnviarEmail"))
              .build();
        }
      }
    }
    return getEnviarEmailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<email.EmailOuterClass.ReceiveRequest,
      email.EmailOuterClass.Email> getRecibirEmailsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RecibirEmails",
      requestType = email.EmailOuterClass.ReceiveRequest.class,
      responseType = email.EmailOuterClass.Email.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<email.EmailOuterClass.ReceiveRequest,
      email.EmailOuterClass.Email> getRecibirEmailsMethod() {
    io.grpc.MethodDescriptor<email.EmailOuterClass.ReceiveRequest, email.EmailOuterClass.Email> getRecibirEmailsMethod;
    if ((getRecibirEmailsMethod = EmailServiceGrpc.getRecibirEmailsMethod) == null) {
      synchronized (EmailServiceGrpc.class) {
        if ((getRecibirEmailsMethod = EmailServiceGrpc.getRecibirEmailsMethod) == null) {
          EmailServiceGrpc.getRecibirEmailsMethod = getRecibirEmailsMethod =
              io.grpc.MethodDescriptor.<email.EmailOuterClass.ReceiveRequest, email.EmailOuterClass.Email>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RecibirEmails"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  email.EmailOuterClass.ReceiveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  email.EmailOuterClass.Email.getDefaultInstance()))
              .setSchemaDescriptor(new EmailServiceMethodDescriptorSupplier("RecibirEmails"))
              .build();
        }
      }
    }
    return getRecibirEmailsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<email.EmailOuterClass.BandejaRequest,
      email.EmailOuterClass.BandejaResponse> getVerBandejaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerBandeja",
      requestType = email.EmailOuterClass.BandejaRequest.class,
      responseType = email.EmailOuterClass.BandejaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<email.EmailOuterClass.BandejaRequest,
      email.EmailOuterClass.BandejaResponse> getVerBandejaMethod() {
    io.grpc.MethodDescriptor<email.EmailOuterClass.BandejaRequest, email.EmailOuterClass.BandejaResponse> getVerBandejaMethod;
    if ((getVerBandejaMethod = EmailServiceGrpc.getVerBandejaMethod) == null) {
      synchronized (EmailServiceGrpc.class) {
        if ((getVerBandejaMethod = EmailServiceGrpc.getVerBandejaMethod) == null) {
          EmailServiceGrpc.getVerBandejaMethod = getVerBandejaMethod =
              io.grpc.MethodDescriptor.<email.EmailOuterClass.BandejaRequest, email.EmailOuterClass.BandejaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerBandeja"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  email.EmailOuterClass.BandejaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  email.EmailOuterClass.BandejaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmailServiceMethodDescriptorSupplier("VerBandeja"))
              .build();
        }
      }
    }
    return getVerBandejaMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EmailServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EmailServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EmailServiceStub>() {
        @java.lang.Override
        public EmailServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EmailServiceStub(channel, callOptions);
        }
      };
    return EmailServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EmailServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EmailServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EmailServiceBlockingStub>() {
        @java.lang.Override
        public EmailServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EmailServiceBlockingStub(channel, callOptions);
        }
      };
    return EmailServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EmailServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EmailServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EmailServiceFutureStub>() {
        @java.lang.Override
        public EmailServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EmailServiceFutureStub(channel, callOptions);
        }
      };
    return EmailServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void enviarEmail(email.EmailOuterClass.EmailRequest request,
        io.grpc.stub.StreamObserver<email.EmailOuterClass.EmailResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEnviarEmailMethod(), responseObserver);
    }

    /**
     */
    default void recibirEmails(email.EmailOuterClass.ReceiveRequest request,
        io.grpc.stub.StreamObserver<email.EmailOuterClass.Email> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecibirEmailsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Nuevo método para ver la bandeja de entrada
     * </pre>
     */
    default void verBandeja(email.EmailOuterClass.BandejaRequest request,
        io.grpc.stub.StreamObserver<email.EmailOuterClass.BandejaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerBandejaMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EmailService.
   */
  public static abstract class EmailServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EmailServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EmailService.
   */
  public static final class EmailServiceStub
      extends io.grpc.stub.AbstractAsyncStub<EmailServiceStub> {
    private EmailServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmailServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EmailServiceStub(channel, callOptions);
    }

    /**
     */
    public void enviarEmail(email.EmailOuterClass.EmailRequest request,
        io.grpc.stub.StreamObserver<email.EmailOuterClass.EmailResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEnviarEmailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recibirEmails(email.EmailOuterClass.ReceiveRequest request,
        io.grpc.stub.StreamObserver<email.EmailOuterClass.Email> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getRecibirEmailsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Nuevo método para ver la bandeja de entrada
     * </pre>
     */
    public void verBandeja(email.EmailOuterClass.BandejaRequest request,
        io.grpc.stub.StreamObserver<email.EmailOuterClass.BandejaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerBandejaMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EmailService.
   */
  public static final class EmailServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EmailServiceBlockingStub> {
    private EmailServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmailServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EmailServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public email.EmailOuterClass.EmailResponse enviarEmail(email.EmailOuterClass.EmailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEnviarEmailMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<email.EmailOuterClass.Email> recibirEmails(
        email.EmailOuterClass.ReceiveRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getRecibirEmailsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Nuevo método para ver la bandeja de entrada
     * </pre>
     */
    public email.EmailOuterClass.BandejaResponse verBandeja(email.EmailOuterClass.BandejaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerBandejaMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EmailService.
   */
  public static final class EmailServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<EmailServiceFutureStub> {
    private EmailServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmailServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EmailServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<email.EmailOuterClass.EmailResponse> enviarEmail(
        email.EmailOuterClass.EmailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEnviarEmailMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Nuevo método para ver la bandeja de entrada
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<email.EmailOuterClass.BandejaResponse> verBandeja(
        email.EmailOuterClass.BandejaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerBandejaMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ENVIAR_EMAIL = 0;
  private static final int METHODID_RECIBIR_EMAILS = 1;
  private static final int METHODID_VER_BANDEJA = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ENVIAR_EMAIL:
          serviceImpl.enviarEmail((email.EmailOuterClass.EmailRequest) request,
              (io.grpc.stub.StreamObserver<email.EmailOuterClass.EmailResponse>) responseObserver);
          break;
        case METHODID_RECIBIR_EMAILS:
          serviceImpl.recibirEmails((email.EmailOuterClass.ReceiveRequest) request,
              (io.grpc.stub.StreamObserver<email.EmailOuterClass.Email>) responseObserver);
          break;
        case METHODID_VER_BANDEJA:
          serviceImpl.verBandeja((email.EmailOuterClass.BandejaRequest) request,
              (io.grpc.stub.StreamObserver<email.EmailOuterClass.BandejaResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getEnviarEmailMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              email.EmailOuterClass.EmailRequest,
              email.EmailOuterClass.EmailResponse>(
                service, METHODID_ENVIAR_EMAIL)))
        .addMethod(
          getRecibirEmailsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              email.EmailOuterClass.ReceiveRequest,
              email.EmailOuterClass.Email>(
                service, METHODID_RECIBIR_EMAILS)))
        .addMethod(
          getVerBandejaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              email.EmailOuterClass.BandejaRequest,
              email.EmailOuterClass.BandejaResponse>(
                service, METHODID_VER_BANDEJA)))
        .build();
  }

  private static abstract class EmailServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EmailServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return email.EmailOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EmailService");
    }
  }

  private static final class EmailServiceFileDescriptorSupplier
      extends EmailServiceBaseDescriptorSupplier {
    EmailServiceFileDescriptorSupplier() {}
  }

  private static final class EmailServiceMethodDescriptorSupplier
      extends EmailServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    EmailServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EmailServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EmailServiceFileDescriptorSupplier())
              .addMethod(getEnviarEmailMethod())
              .addMethod(getRecibirEmailsMethod())
              .addMethod(getVerBandejaMethod())
              .build();
        }
      }
    }
    return result;
  }
}

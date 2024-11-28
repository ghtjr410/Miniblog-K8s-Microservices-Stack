/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.miniblog.viewcount.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class ViewcountUpdatedEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -8471334465194796755L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ViewcountUpdatedEvent\",\"namespace\":\"com.miniblog.viewcount.avro\",\"fields\":[{\"name\":\"postUuid\",\"type\":\"string\"},{\"name\":\"totalViews\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ViewcountUpdatedEvent> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ViewcountUpdatedEvent> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ViewcountUpdatedEvent> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ViewcountUpdatedEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ViewcountUpdatedEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ViewcountUpdatedEvent to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ViewcountUpdatedEvent from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ViewcountUpdatedEvent instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ViewcountUpdatedEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence postUuid;
  private long totalViews;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ViewcountUpdatedEvent() {}

  /**
   * All-args constructor.
   * @param postUuid The new value for postUuid
   * @param totalViews The new value for totalViews
   */
  public ViewcountUpdatedEvent(java.lang.CharSequence postUuid, java.lang.Long totalViews) {
    this.postUuid = postUuid;
    this.totalViews = totalViews;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return postUuid;
    case 1: return totalViews;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: postUuid = (java.lang.CharSequence)value$; break;
    case 1: totalViews = (java.lang.Long)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'postUuid' field.
   * @return The value of the 'postUuid' field.
   */
  public java.lang.CharSequence getPostUuid() {
    return postUuid;
  }


  /**
   * Sets the value of the 'postUuid' field.
   * @param value the value to set.
   */
  public void setPostUuid(java.lang.CharSequence value) {
    this.postUuid = value;
  }

  /**
   * Gets the value of the 'totalViews' field.
   * @return The value of the 'totalViews' field.
   */
  public long getTotalViews() {
    return totalViews;
  }


  /**
   * Sets the value of the 'totalViews' field.
   * @param value the value to set.
   */
  public void setTotalViews(long value) {
    this.totalViews = value;
  }

  /**
   * Creates a new ViewcountUpdatedEvent RecordBuilder.
   * @return A new ViewcountUpdatedEvent RecordBuilder
   */
  public static com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder newBuilder() {
    return new com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder();
  }

  /**
   * Creates a new ViewcountUpdatedEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ViewcountUpdatedEvent RecordBuilder
   */
  public static com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder newBuilder(com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder other) {
    if (other == null) {
      return new com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder();
    } else {
      return new com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder(other);
    }
  }

  /**
   * Creates a new ViewcountUpdatedEvent RecordBuilder by copying an existing ViewcountUpdatedEvent instance.
   * @param other The existing instance to copy.
   * @return A new ViewcountUpdatedEvent RecordBuilder
   */
  public static com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder newBuilder(com.miniblog.viewcount.avro.ViewcountUpdatedEvent other) {
    if (other == null) {
      return new com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder();
    } else {
      return new com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder(other);
    }
  }

  /**
   * RecordBuilder for ViewcountUpdatedEvent instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ViewcountUpdatedEvent>
    implements org.apache.avro.data.RecordBuilder<ViewcountUpdatedEvent> {

    private java.lang.CharSequence postUuid;
    private long totalViews;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.postUuid)) {
        this.postUuid = data().deepCopy(fields()[0].schema(), other.postUuid);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.totalViews)) {
        this.totalViews = data().deepCopy(fields()[1].schema(), other.totalViews);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing ViewcountUpdatedEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(com.miniblog.viewcount.avro.ViewcountUpdatedEvent other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.postUuid)) {
        this.postUuid = data().deepCopy(fields()[0].schema(), other.postUuid);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.totalViews)) {
        this.totalViews = data().deepCopy(fields()[1].schema(), other.totalViews);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'postUuid' field.
      * @return The value.
      */
    public java.lang.CharSequence getPostUuid() {
      return postUuid;
    }


    /**
      * Sets the value of the 'postUuid' field.
      * @param value The value of 'postUuid'.
      * @return This builder.
      */
    public com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder setPostUuid(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.postUuid = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'postUuid' field has been set.
      * @return True if the 'postUuid' field has been set, false otherwise.
      */
    public boolean hasPostUuid() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'postUuid' field.
      * @return This builder.
      */
    public com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder clearPostUuid() {
      postUuid = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'totalViews' field.
      * @return The value.
      */
    public long getTotalViews() {
      return totalViews;
    }


    /**
      * Sets the value of the 'totalViews' field.
      * @param value The value of 'totalViews'.
      * @return This builder.
      */
    public com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder setTotalViews(long value) {
      validate(fields()[1], value);
      this.totalViews = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'totalViews' field has been set.
      * @return True if the 'totalViews' field has been set, false otherwise.
      */
    public boolean hasTotalViews() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'totalViews' field.
      * @return This builder.
      */
    public com.miniblog.viewcount.avro.ViewcountUpdatedEvent.Builder clearTotalViews() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ViewcountUpdatedEvent build() {
      try {
        ViewcountUpdatedEvent record = new ViewcountUpdatedEvent();
        record.postUuid = fieldSetFlags()[0] ? this.postUuid : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.totalViews = fieldSetFlags()[1] ? this.totalViews : (java.lang.Long) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ViewcountUpdatedEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<ViewcountUpdatedEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ViewcountUpdatedEvent>
    READER$ = (org.apache.avro.io.DatumReader<ViewcountUpdatedEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.postUuid);

    out.writeLong(this.totalViews);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.postUuid = in.readString(this.postUuid instanceof Utf8 ? (Utf8)this.postUuid : null);

      this.totalViews = in.readLong();

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.postUuid = in.readString(this.postUuid instanceof Utf8 ? (Utf8)this.postUuid : null);
          break;

        case 1:
          this.totalViews = in.readLong();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











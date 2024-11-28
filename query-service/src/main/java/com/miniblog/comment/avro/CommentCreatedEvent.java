/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.miniblog.comment.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class CommentCreatedEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -4677171280288286409L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"CommentCreatedEvent\",\"namespace\":\"com.miniblog.comment.avro\",\"fields\":[{\"name\":\"commentUuid\",\"type\":\"string\"},{\"name\":\"postUuid\",\"type\":\"string\"},{\"name\":\"userUuid\",\"type\":\"string\"},{\"name\":\"nickname\",\"type\":\"string\"},{\"name\":\"content\",\"type\":\"string\"},{\"name\":\"createdDate\",\"type\":\"long\"},{\"name\":\"updatedDate\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<CommentCreatedEvent> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<CommentCreatedEvent> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<CommentCreatedEvent> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<CommentCreatedEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<CommentCreatedEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this CommentCreatedEvent to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a CommentCreatedEvent from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a CommentCreatedEvent instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static CommentCreatedEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence commentUuid;
  private java.lang.CharSequence postUuid;
  private java.lang.CharSequence userUuid;
  private java.lang.CharSequence nickname;
  private java.lang.CharSequence content;
  private long createdDate;
  private long updatedDate;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public CommentCreatedEvent() {}

  /**
   * All-args constructor.
   * @param commentUuid The new value for commentUuid
   * @param postUuid The new value for postUuid
   * @param userUuid The new value for userUuid
   * @param nickname The new value for nickname
   * @param content The new value for content
   * @param createdDate The new value for createdDate
   * @param updatedDate The new value for updatedDate
   */
  public CommentCreatedEvent(java.lang.CharSequence commentUuid, java.lang.CharSequence postUuid, java.lang.CharSequence userUuid, java.lang.CharSequence nickname, java.lang.CharSequence content, java.lang.Long createdDate, java.lang.Long updatedDate) {
    this.commentUuid = commentUuid;
    this.postUuid = postUuid;
    this.userUuid = userUuid;
    this.nickname = nickname;
    this.content = content;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return commentUuid;
    case 1: return postUuid;
    case 2: return userUuid;
    case 3: return nickname;
    case 4: return content;
    case 5: return createdDate;
    case 6: return updatedDate;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: commentUuid = (java.lang.CharSequence)value$; break;
    case 1: postUuid = (java.lang.CharSequence)value$; break;
    case 2: userUuid = (java.lang.CharSequence)value$; break;
    case 3: nickname = (java.lang.CharSequence)value$; break;
    case 4: content = (java.lang.CharSequence)value$; break;
    case 5: createdDate = (java.lang.Long)value$; break;
    case 6: updatedDate = (java.lang.Long)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'commentUuid' field.
   * @return The value of the 'commentUuid' field.
   */
  public java.lang.CharSequence getCommentUuid() {
    return commentUuid;
  }


  /**
   * Sets the value of the 'commentUuid' field.
   * @param value the value to set.
   */
  public void setCommentUuid(java.lang.CharSequence value) {
    this.commentUuid = value;
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
   * Gets the value of the 'userUuid' field.
   * @return The value of the 'userUuid' field.
   */
  public java.lang.CharSequence getUserUuid() {
    return userUuid;
  }


  /**
   * Sets the value of the 'userUuid' field.
   * @param value the value to set.
   */
  public void setUserUuid(java.lang.CharSequence value) {
    this.userUuid = value;
  }

  /**
   * Gets the value of the 'nickname' field.
   * @return The value of the 'nickname' field.
   */
  public java.lang.CharSequence getNickname() {
    return nickname;
  }


  /**
   * Sets the value of the 'nickname' field.
   * @param value the value to set.
   */
  public void setNickname(java.lang.CharSequence value) {
    this.nickname = value;
  }

  /**
   * Gets the value of the 'content' field.
   * @return The value of the 'content' field.
   */
  public java.lang.CharSequence getContent() {
    return content;
  }


  /**
   * Sets the value of the 'content' field.
   * @param value the value to set.
   */
  public void setContent(java.lang.CharSequence value) {
    this.content = value;
  }

  /**
   * Gets the value of the 'createdDate' field.
   * @return The value of the 'createdDate' field.
   */
  public long getCreatedDate() {
    return createdDate;
  }


  /**
   * Sets the value of the 'createdDate' field.
   * @param value the value to set.
   */
  public void setCreatedDate(long value) {
    this.createdDate = value;
  }

  /**
   * Gets the value of the 'updatedDate' field.
   * @return The value of the 'updatedDate' field.
   */
  public long getUpdatedDate() {
    return updatedDate;
  }


  /**
   * Sets the value of the 'updatedDate' field.
   * @param value the value to set.
   */
  public void setUpdatedDate(long value) {
    this.updatedDate = value;
  }

  /**
   * Creates a new CommentCreatedEvent RecordBuilder.
   * @return A new CommentCreatedEvent RecordBuilder
   */
  public static com.miniblog.comment.avro.CommentCreatedEvent.Builder newBuilder() {
    return new com.miniblog.comment.avro.CommentCreatedEvent.Builder();
  }

  /**
   * Creates a new CommentCreatedEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new CommentCreatedEvent RecordBuilder
   */
  public static com.miniblog.comment.avro.CommentCreatedEvent.Builder newBuilder(com.miniblog.comment.avro.CommentCreatedEvent.Builder other) {
    if (other == null) {
      return new com.miniblog.comment.avro.CommentCreatedEvent.Builder();
    } else {
      return new com.miniblog.comment.avro.CommentCreatedEvent.Builder(other);
    }
  }

  /**
   * Creates a new CommentCreatedEvent RecordBuilder by copying an existing CommentCreatedEvent instance.
   * @param other The existing instance to copy.
   * @return A new CommentCreatedEvent RecordBuilder
   */
  public static com.miniblog.comment.avro.CommentCreatedEvent.Builder newBuilder(com.miniblog.comment.avro.CommentCreatedEvent other) {
    if (other == null) {
      return new com.miniblog.comment.avro.CommentCreatedEvent.Builder();
    } else {
      return new com.miniblog.comment.avro.CommentCreatedEvent.Builder(other);
    }
  }

  /**
   * RecordBuilder for CommentCreatedEvent instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<CommentCreatedEvent>
    implements org.apache.avro.data.RecordBuilder<CommentCreatedEvent> {

    private java.lang.CharSequence commentUuid;
    private java.lang.CharSequence postUuid;
    private java.lang.CharSequence userUuid;
    private java.lang.CharSequence nickname;
    private java.lang.CharSequence content;
    private long createdDate;
    private long updatedDate;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.miniblog.comment.avro.CommentCreatedEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.commentUuid)) {
        this.commentUuid = data().deepCopy(fields()[0].schema(), other.commentUuid);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.postUuid)) {
        this.postUuid = data().deepCopy(fields()[1].schema(), other.postUuid);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.userUuid)) {
        this.userUuid = data().deepCopy(fields()[2].schema(), other.userUuid);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.nickname)) {
        this.nickname = data().deepCopy(fields()[3].schema(), other.nickname);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.content)) {
        this.content = data().deepCopy(fields()[4].schema(), other.content);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.createdDate)) {
        this.createdDate = data().deepCopy(fields()[5].schema(), other.createdDate);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
      if (isValidValue(fields()[6], other.updatedDate)) {
        this.updatedDate = data().deepCopy(fields()[6].schema(), other.updatedDate);
        fieldSetFlags()[6] = other.fieldSetFlags()[6];
      }
    }

    /**
     * Creates a Builder by copying an existing CommentCreatedEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(com.miniblog.comment.avro.CommentCreatedEvent other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.commentUuid)) {
        this.commentUuid = data().deepCopy(fields()[0].schema(), other.commentUuid);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.postUuid)) {
        this.postUuid = data().deepCopy(fields()[1].schema(), other.postUuid);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.userUuid)) {
        this.userUuid = data().deepCopy(fields()[2].schema(), other.userUuid);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.nickname)) {
        this.nickname = data().deepCopy(fields()[3].schema(), other.nickname);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.content)) {
        this.content = data().deepCopy(fields()[4].schema(), other.content);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.createdDate)) {
        this.createdDate = data().deepCopy(fields()[5].schema(), other.createdDate);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.updatedDate)) {
        this.updatedDate = data().deepCopy(fields()[6].schema(), other.updatedDate);
        fieldSetFlags()[6] = true;
      }
    }

    /**
      * Gets the value of the 'commentUuid' field.
      * @return The value.
      */
    public java.lang.CharSequence getCommentUuid() {
      return commentUuid;
    }


    /**
      * Sets the value of the 'commentUuid' field.
      * @param value The value of 'commentUuid'.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setCommentUuid(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.commentUuid = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'commentUuid' field has been set.
      * @return True if the 'commentUuid' field has been set, false otherwise.
      */
    public boolean hasCommentUuid() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'commentUuid' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearCommentUuid() {
      commentUuid = null;
      fieldSetFlags()[0] = false;
      return this;
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
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setPostUuid(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.postUuid = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'postUuid' field has been set.
      * @return True if the 'postUuid' field has been set, false otherwise.
      */
    public boolean hasPostUuid() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'postUuid' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearPostUuid() {
      postUuid = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'userUuid' field.
      * @return The value.
      */
    public java.lang.CharSequence getUserUuid() {
      return userUuid;
    }


    /**
      * Sets the value of the 'userUuid' field.
      * @param value The value of 'userUuid'.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setUserUuid(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.userUuid = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'userUuid' field has been set.
      * @return True if the 'userUuid' field has been set, false otherwise.
      */
    public boolean hasUserUuid() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'userUuid' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearUserUuid() {
      userUuid = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'nickname' field.
      * @return The value.
      */
    public java.lang.CharSequence getNickname() {
      return nickname;
    }


    /**
      * Sets the value of the 'nickname' field.
      * @param value The value of 'nickname'.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setNickname(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.nickname = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'nickname' field has been set.
      * @return True if the 'nickname' field has been set, false otherwise.
      */
    public boolean hasNickname() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'nickname' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearNickname() {
      nickname = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'content' field.
      * @return The value.
      */
    public java.lang.CharSequence getContent() {
      return content;
    }


    /**
      * Sets the value of the 'content' field.
      * @param value The value of 'content'.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setContent(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.content = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'content' field has been set.
      * @return True if the 'content' field has been set, false otherwise.
      */
    public boolean hasContent() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'content' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearContent() {
      content = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'createdDate' field.
      * @return The value.
      */
    public long getCreatedDate() {
      return createdDate;
    }


    /**
      * Sets the value of the 'createdDate' field.
      * @param value The value of 'createdDate'.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setCreatedDate(long value) {
      validate(fields()[5], value);
      this.createdDate = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'createdDate' field has been set.
      * @return True if the 'createdDate' field has been set, false otherwise.
      */
    public boolean hasCreatedDate() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'createdDate' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearCreatedDate() {
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'updatedDate' field.
      * @return The value.
      */
    public long getUpdatedDate() {
      return updatedDate;
    }


    /**
      * Sets the value of the 'updatedDate' field.
      * @param value The value of 'updatedDate'.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder setUpdatedDate(long value) {
      validate(fields()[6], value);
      this.updatedDate = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'updatedDate' field has been set.
      * @return True if the 'updatedDate' field has been set, false otherwise.
      */
    public boolean hasUpdatedDate() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'updatedDate' field.
      * @return This builder.
      */
    public com.miniblog.comment.avro.CommentCreatedEvent.Builder clearUpdatedDate() {
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommentCreatedEvent build() {
      try {
        CommentCreatedEvent record = new CommentCreatedEvent();
        record.commentUuid = fieldSetFlags()[0] ? this.commentUuid : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.postUuid = fieldSetFlags()[1] ? this.postUuid : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.userUuid = fieldSetFlags()[2] ? this.userUuid : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.nickname = fieldSetFlags()[3] ? this.nickname : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.content = fieldSetFlags()[4] ? this.content : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.createdDate = fieldSetFlags()[5] ? this.createdDate : (java.lang.Long) defaultValue(fields()[5]);
        record.updatedDate = fieldSetFlags()[6] ? this.updatedDate : (java.lang.Long) defaultValue(fields()[6]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<CommentCreatedEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<CommentCreatedEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<CommentCreatedEvent>
    READER$ = (org.apache.avro.io.DatumReader<CommentCreatedEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.commentUuid);

    out.writeString(this.postUuid);

    out.writeString(this.userUuid);

    out.writeString(this.nickname);

    out.writeString(this.content);

    out.writeLong(this.createdDate);

    out.writeLong(this.updatedDate);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.commentUuid = in.readString(this.commentUuid instanceof Utf8 ? (Utf8)this.commentUuid : null);

      this.postUuid = in.readString(this.postUuid instanceof Utf8 ? (Utf8)this.postUuid : null);

      this.userUuid = in.readString(this.userUuid instanceof Utf8 ? (Utf8)this.userUuid : null);

      this.nickname = in.readString(this.nickname instanceof Utf8 ? (Utf8)this.nickname : null);

      this.content = in.readString(this.content instanceof Utf8 ? (Utf8)this.content : null);

      this.createdDate = in.readLong();

      this.updatedDate = in.readLong();

    } else {
      for (int i = 0; i < 7; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.commentUuid = in.readString(this.commentUuid instanceof Utf8 ? (Utf8)this.commentUuid : null);
          break;

        case 1:
          this.postUuid = in.readString(this.postUuid instanceof Utf8 ? (Utf8)this.postUuid : null);
          break;

        case 2:
          this.userUuid = in.readString(this.userUuid instanceof Utf8 ? (Utf8)this.userUuid : null);
          break;

        case 3:
          this.nickname = in.readString(this.nickname instanceof Utf8 ? (Utf8)this.nickname : null);
          break;

        case 4:
          this.content = in.readString(this.content instanceof Utf8 ? (Utf8)this.content : null);
          break;

        case 5:
          this.createdDate = in.readLong();
          break;

        case 6:
          this.updatedDate = in.readLong();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











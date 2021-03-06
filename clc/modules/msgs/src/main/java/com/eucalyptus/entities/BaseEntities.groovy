package com.eucalyptus.entities;

import java.io.Serializable
import java.util.Date
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Transient
import javax.persistence.Version
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NaturalId
import com.eucalyptus.util.HasNaturalId


@MappedSuperclass
public class AbstractPersistent implements Serializable, HasNaturalId {
  @Transient
  private static final long serialVersionUID = 1;
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  @Column( name = "id" )
  String id;
  @Version
  @Column(name = "version")
  Integer version;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_timestamp")
  Date creationTimestamp;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_update_timestamp")
  Date lastUpdateTimestamp;
  @NaturalId
  @Column( name = "metadata_perm_uuid", unique = true, updatable = false, nullable = false )
  private String   naturalId;
  
  public AbstractPersistent( ) {
    super( );
  }
  
  @Override
  public int hashCode( ) {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null ) ? 0 : id.hashCode( ) );
    return result;
  }
  
  @Override
  public boolean equals( Object obj ) {
    if ( this.is( obj ) ) return true;
    if ( obj == null ) return false;
    if ( !getClass( ).is( obj.getClass( ) ) ) return false;
    AbstractPersistent other = ( AbstractPersistent ) obj;
    if ( this.naturalId == null ) {
      return other.naturalId != null;
    } else if ( !naturalId.equals( other.naturalId ) ) {
      return false;
    } else {
      return true;
    }
  }
  
  @PreUpdate
  @PrePersist
  public void updateTimeStamps() {
    lastUpdateTimestamp = new Date();
    if ( creationTimestamp == null ) {
      this.creationTimestamp = new Date();
    }
    if ( this.naturalId == null ) {
      this.naturalId = UUID.randomUUID( ).toString( );
    }
  }
  
  @Override
  public String getNaturalId( ) {
    return this.naturalId;
  }
  
  protected void setNaturalId( String naturalId ) {
    this.naturalId = naturalId;
  }
  
  /**
   * NOTE:IMPORTANT: this value is controlled by the persistence layer/db and should not be relied for stability or references outside the database.  For example, it should not be the identifier for users to refer to the object.
   * @return autogenerated entity identifier
   */
  protected final String getId( ) {
    return this.id;
  }
  
  public long lastUpdateMillis( ) {
    return System.currentTimeMillis( ) - ( this.getLastUpdateTimestamp( ) != null ? this.getLastUpdateTimestamp( ).getTime( ) : 0L );
  }
}


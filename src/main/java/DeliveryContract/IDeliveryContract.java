package DeliveryContract;

import java.io.Serializable;

public interface IDeliveryContract extends Serializable {

  //  boolean create(Object object);
    Object get();

  /**
   * Returnes the Object state.
   *
   * @return    true if it is created, false if not.
   */
  boolean getIsCreated();

  /**
   * Sets the Object state.
   *
   * @param isCreated    true if it is created, false if it is not created.
   */
  void setIsCreated(boolean isCreated);
}

package DeliveryContract;

public interface IContractComponent {

    Object create(Object object);
    Object get();

    boolean isCreated();
}

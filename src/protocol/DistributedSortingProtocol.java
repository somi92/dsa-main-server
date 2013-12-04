package protocol;

public interface DistributedSortingProtocol {

	public int parseProtocolMessage(String message);
	public String generateResponse();
//	public String generateRespond(String message);
}

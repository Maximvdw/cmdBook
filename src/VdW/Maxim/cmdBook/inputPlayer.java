package VdW.Maxim.cmdBook;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class inputPlayer extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "" + context.getSessionData("data");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String in) {
		 if(!in.equalsIgnoreCase(null)){
				context.setSessionData("data", in);
				return END_OF_CONVERSATION; 
		 }
		 return this;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String in) {
		return true;
	}

}
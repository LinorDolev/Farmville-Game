package farmground.logic.plugins;

import farmground.logic.ActivityEntity;

public interface Plugin {
	public Object execute(ActivityEntity command) throws Exception;
}

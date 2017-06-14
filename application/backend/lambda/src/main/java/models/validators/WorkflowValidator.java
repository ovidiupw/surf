package models.validators;

import com.amazonaws.services.lambda.runtime.Context;
import models.Workflow;
import utils.Logger;

import javax.annotation.Nonnull;

public class WorkflowValidator implements Validator<Workflow> {

    private final Context context;

    public WorkflowValidator(@Nonnull final Context context) {
        this.context = context;
    }

    @Override
    public void validate(@Nonnull final Workflow workflow) {
        Logger.LOG(context.getLogger(), "Validating workflow '%s'...", workflow);
    }
}

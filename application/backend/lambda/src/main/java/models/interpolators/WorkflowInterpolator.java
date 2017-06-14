package models.interpolators;

import com.amazonaws.services.lambda.runtime.Context;
import models.Workflow;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;
import utils.RandomGenerator;

import javax.annotation.Nonnull;

public class WorkflowInterpolator implements Interpolator<Workflow> {

    private final Context context;

    public WorkflowInterpolator(@Nonnull final Context context, @Nonnull final String userArn) {
        this.context = context;
    }

    @Override
    public Workflow interpolate(@Nonnull final Workflow workflow) throws BadRequestException, InternalServerException {
        Logger.LOG(context.getLogger(), "Interpolating workflow '%s'...", workflow);

        final Workflow interpolatedWorkflow = new Workflow();
        interpolatedWorkflow.setCreationDateMillis(System.currentTimeMillis());
        interpolatedWorkflow.setId(
                RandomGenerator.randomUUIDWithSeparatedSuffix(
                        "-", String.valueOf(interpolatedWorkflow.getCreationDateMillis())));
        //interpolatedWorkflow.setOwnerId();

        return interpolatedWorkflow;
    }
}

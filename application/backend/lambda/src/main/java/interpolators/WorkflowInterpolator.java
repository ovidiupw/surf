package interpolators;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.Workflow;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;
import utils.RandomGenerator;
import utils.SurfObjectMother;

import javax.annotation.Nonnull;

public class WorkflowInterpolator implements Interpolator<Workflow> {

    private final Context context;
    private final String userArn;

    public WorkflowInterpolator(@Nonnull final Context context, @Nonnull final String userArn) {
        this.context = context;
        this.userArn = userArn;
    }

    @Override
    public Workflow interpolate(@Nonnull final Workflow workflow) throws BadRequestException, InternalServerException {
        Logger.log(context.getLogger(), "Interpolating workflow '%s'...", workflow);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(userArn), "The userArn cannot be null or empty!");

        final Workflow interpolatedWorkflow = new Workflow();
        interpolatedWorkflow.setCreationDateMillis(System.currentTimeMillis());
        interpolatedWorkflow.setId(RandomGenerator.randomUUID());

        final String fullOwnerId = SurfObjectMother.getOwnerId(userArn);

        interpolatedWorkflow.setOwnerId(fullOwnerId);
        interpolatedWorkflow.setName(workflow.getName());
        interpolatedWorkflow.setMetadata(workflow.getMetadata()); // TODO deep copy

        Logger.log(context.getLogger(), "Successfully interpolated workflow. Result is: '%s'.", workflow);
        return interpolatedWorkflow;
    }
}

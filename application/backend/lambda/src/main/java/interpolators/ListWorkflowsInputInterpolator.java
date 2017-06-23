package interpolators;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.base.Preconditions;
import handlers.ListWorkflowsHandler;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;

import javax.annotation.Nonnull;

public class ListWorkflowsInputInterpolator implements Interpolator<ListWorkflowsHandler.Input> {

    private final Logger LOG;

    public ListWorkflowsInputInterpolator(@Nonnull final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public ListWorkflowsHandler.Input interpolate(@Nonnull final ListWorkflowsHandler.Input input)
            throws BadRequestException, InternalServerException {
        LOG.info("Interpolating ListWorkflowsHandler.Input '%s'...", input);
        Preconditions.checkNotNull(input);

        final ListWorkflowsHandler.Input interpolatedInput = new ListWorkflowsHandler.Input();
        //TODO deep copy
        interpolatedInput.setUserArn(input.getUserArn());
        interpolatedInput.setResultsPerPage(input.getResultsPerPage());
        interpolatedInput.setCreatedBefore(input.getCreatedBefore());
        if (input.getStartingWorkflowId().isEmpty()) {
            LOG.info("StartingWorkflowId was empty! Setting it to null...");
            interpolatedInput.setStartingWorkflowId(null);
        } else {
            LOG.info("StartingWorkflowId was not empty, but '%s'", input.getStartingWorkflowId());
            interpolatedInput.setStartingWorkflowId(input.getStartingWorkflowId());
        }

        LOG.info("Successfully interpolated ListWorkflowsHandler.Input. Result is: '%s'.", interpolatedInput);
        return interpolatedInput;
    }
}

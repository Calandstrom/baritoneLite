/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.RelativeCoordinate;
import baritone.api.command.datatypes.RelativeGoal;
import baritone.api.command.exception.CommandException;
import baritone.api.pathing.goals.Goal;
import baritone.api.utils.BetterBlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GotoCommand extends Command {

    protected GotoCommand(IBaritone baritone) {
        super(baritone, "goto");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        // Require at least X and Z, at most X Y Z
        args.requireMin(2);
        args.requireMax(3);

        // Make sure the first arg looks like a coordinate
        if (args.peekDatatypeOrNull(RelativeCoordinate.INSTANCE) == null) {
            logDirect("Invalid arguments: goto only supports coordinates (x z or x y z)");
        }

        BetterBlockPos origin = ctx.playerFeet();
        Goal goal = args.getDatatypePost(RelativeGoal.INSTANCE, origin);

        logDirect(String.format("Going to: %s", goal.toString()));
        baritone.getCustomGoalProcess().setGoalAndPath(goal);
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        // No block completions, just return empty
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Go to a coordinate (x z or x y z)";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
            "The goto command tells Baritone to head towards a specific coordinate.",
            "",
            "Coordinates can be absolute (numbers) or relative (~ like in Minecraft commands).",
            "",
            "Usage:",
            "> goto <x> <z>",
            "> goto <x> <y> <z>"
        );
    }
}

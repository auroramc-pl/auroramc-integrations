package pl.auroramc.integrations.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.velocity.LiteVelocitySettings;
import dev.rollczi.litecommands.velocity.tools.VelocityOnlyPlayerContextual;
import pl.auroramc.integrations.commands.argument.resolver.PlayerArgumentResolver;
import pl.auroramc.integrations.commands.permission.DefaultMissingPermissionsHandler;
import pl.auroramc.integrations.configs.command.CommandMessageSource;
import pl.auroramc.messages.message.compiler.MessageCompiler;

public class VelocityCommandsBuilderProcessor
    extends CommandsBuilderProcessor<CommandSource, LiteVelocitySettings> {

  private final ProxyServer server;
  private final CommandMessageSource messageSource;
  private final MessageCompiler<CommandSource> messageCompiler;

  public VelocityCommandsBuilderProcessor(
      final ProxyServer server,
      final CommandMessageSource messageSource,
      final MessageCompiler<CommandSource> messageCompiler) {
    super(messageSource, messageCompiler);
    this.server = server;
    this.messageSource = messageSource;
    this.messageCompiler = messageCompiler;
  }

  @Override
  public void process(
      final LiteCommandsBuilder<CommandSource, LiteVelocitySettings, ?> builder,
      final LiteCommandsInternal<CommandSource, LiteVelocitySettings> internal) {
    super.process(builder, internal);
    builder
        .context(
            Player.class,
            new VelocityOnlyPlayerContextual<>(messageSource.executionFromConsoleIsUnsupported))
        .result(
            MissingPermissions.class,
            new DefaultMissingPermissionsHandler<>(messageSource, messageCompiler))
        .argument(Player.class, new PlayerArgumentResolver<>(server, messageSource));
  }
}

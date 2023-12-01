package eu.pb4.brewery.other;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;


public record WrappedText(Text text, @Nullable String input) {
    public static final Codec<WrappedText> CODEC = Codec.either(Codec.STRING, TextCodecs.CODEC)
            .xmap(x -> x.left().isPresent() ? of(x.left().get()) : of(x.right().get()), x -> x.input != null ? Either.left(x.input) : Either.right(x.text));

    public static WrappedText of(String input) {
        return new WrappedText(TextParserUtils.formatText(input), input);
    }

    public static WrappedText of(Text text) {
        return new WrappedText(text, null);
    }
}

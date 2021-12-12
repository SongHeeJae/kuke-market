package kukekyakya.kukemarket.dto.post;

import kukekyakya.kukemarket.entity.post.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
    private String filename;
    private String filePath;
    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getOriginName(),image.getPath() + image.getUniqueName());
    }
}

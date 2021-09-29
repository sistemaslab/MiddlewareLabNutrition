package com.middleware.provider.inventory.model.rest;

import java.util.Date;



import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseSearchInventoryComplete {
	private List<ResponseSearchInventory> data;
        private long total;

    public ResponseSearchInventoryComplete(List<ResponseSearchInventory> data, long total) {
        this.data = data;
        this.total = total;
    }

}

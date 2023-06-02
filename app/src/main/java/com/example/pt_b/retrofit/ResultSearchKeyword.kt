package com.example.pt_b.retrofit




data class ResultSearchKeyword(
    var response: ResponseData?,
    var message: String?
)

data class ResponseData(
    var searchItems: List<ItemInfo>?
)

data class ItemInfo(
    var name: String?,
    var imgUrl: String?,
    var brand: String?,
    var promotion: String?,
    var pricePerUnit: Int?,
    var pricePerGroup: Int?
)

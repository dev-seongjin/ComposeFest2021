package com.seongjin.layoutsincompose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//chap8. Complex custom layout
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        //각 행의 너비를 트랙킹 하기 위한 배열
        val rowWidths = IntArray(rows) { 0 }

        //각 행마다의 최고 높이를 트랙킹 하기 위한 배열
        val rowHeights = IntArray(rows) { 0 }

        //constraint를 기준으로 자식 뷰를 측정 (두번 이상 측정 금지)
        //측정된 자식들의 목록
        val placables = measurables.mapIndexed { index, measurable ->
            //각 자식을 측정
            val placeable = measurable.measure(constraints)

            //각 행의 너비와 최고 높이를 트랙킹
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)
            placeable
        }

        //그리드의 너비는 가장 긴 행의 너비
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) //최고 너비를 constraint의 최소 ~ 최대 너비 범위로 자름
            ?: constraints.minWidth //최고 너비가 없다면 constraint의 최소 너비를 그리드의 너비로 지정

        //그리드의 높이는 각 행의 최고 높이들을 더한 값
        //높이 constraint 범위로 높이 범위를 자름
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        //height = 12
        //rowheights[5, 1, 2, 4]
        //           0, 1, 2, 3
        //row[0] = 0 -> 0~5
        //row[1] = 0 + 5 = 5 -> 5~6
        //row[2] = 5 + 1 = 6 -> 6~8
        //row[3] = 6 + 2 = 8 -> 8~12

        //각 행의 높이와, 각 행 이전 높이를 계산하여 행의 y position 구하기
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1] //이전행 높이 시작 + 이전 행이 표현될 공간
        }

        //부모 레이아웃의 사이즈 지정
        layout(width, height) {
            //각 행 마다 자식이 놓일 x 좌표들을 설정
            val rowX = IntArray(rows) { 0 }
            placables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}


@Composable
fun BodyContentStaggeredGrid(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .background(color = Color.LightGray, shape = RectangleShape)
        .padding(16.dp)
        .size(200.dp) //chaining 순서에 따라 width 달라짐
        .horizontalScroll(rememberScrollState())) {
        StaggeredGrid {
            topics.map { topic ->
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

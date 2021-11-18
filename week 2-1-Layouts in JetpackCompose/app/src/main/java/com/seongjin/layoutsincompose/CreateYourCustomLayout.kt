package com.seongjin.layoutsincompose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seongjin.layoutsincompose.ui.theme.LayoutsInComposeTheme

//chap7. Create your Custom layout
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp //패딩
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints) //constraint에 따라 child 측정
        //composable이 first baseline을 가지고 있는지 확인
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseLine = placeable[FirstBaseline]

        //padding - firstBaseline으로 composable이 놓일 y값을 구함
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseLine
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            //composable이 놓일 위치
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Composable
fun MyOwnColumn(
    modifier : Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        //주어진 constraint로 child 뷰의 높이, 넓이, 사이즈 등을 측정
        //한번이상 측정하면 성능 저하

        //측정된 자식들의 목록
        val placeables = measurables.map { measurable ->
            //자식 측정
            measurable.measure(constraints)
        }

        //자식들을 위 아래로 배치하기 위한 y 좌표
        var yPosition = 0

        //가능한 가장 큰 사이즈를 기준으로 함
        layout(constraints.maxWidth, constraints.maxHeight) {
            //부모 레이아웃에 자식 배치
            placeables.forEach { placeable ->
                placeable.placeRelative(0, yPosition)
                //y position 증가
                yPosition += placeable.height
            }
        }
    }
}

//chap7. Create your Custom layout
@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutsInComposeTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}



//chap7. Create your Custom layout
@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutsInComposeTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}

